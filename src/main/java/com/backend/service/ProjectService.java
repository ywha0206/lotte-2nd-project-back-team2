package com.backend.service;

import com.backend.dto.request.project.PatchCoworkersDTO;
import com.backend.dto.request.project.PostProjectDTO;
import com.backend.dto.response.admin.project.GetProjectLeaderDto;
import com.backend.dto.response.admin.project.GetProjects;
import com.backend.dto.response.project.*;
import com.backend.dto.response.user.GetUsersAllDto;
import com.backend.entity.group.Group;
import com.backend.entity.group.GroupMapper;
import com.backend.entity.project.*;
import com.backend.entity.user.User;
import com.backend.repository.GroupRepository;
import com.backend.repository.UserRepository;
import com.backend.repository.project.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/*

    이름 : 김주경
    날짜 : 2024/12/03
    작업내용 : 프로젝트 생성

    수정이력
        - 2024/12/04 코드 간편화, 프로젝트 불러오기
        - 2024/12/05 프로젝트 컬럼 추가, 수정
        - 2024/12/06 작업자 수정
        - 2024/12/09 태스크 삭제, 템플릿 프로젝트 생성시 컬럼/태스크 등 추가 등록

 */

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    private final ProjectCoworkerRepository coworkerRepository;
    private final ProjectColumnRepository columnRepository;
    private final ProjectTaskRepository taskRepository;
    private final ProjectSubTaskRepository subTaskRepository;
    private final ProjectAssignRepository assignRepository;
    private final ProjectCommentRepository commentRepository;


    private final SimpMessagingTemplate messagingTemplate;

    public void sendBoardUpdate(Long projectId, String eventType, Object payload) {
        String destination = "/topic/project/" + projectId + "/update";
        WebSocketEventMessage message = new WebSocketEventMessage(eventType, payload);
        messagingTemplate.convertAndSend(destination, message);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Project createProject(PostProjectDTO postDTO, String username) {

        List<GetProjectCoworkerDTO> userList = postDTO.getCoworkers();
        Project project = projectRepository.save(postDTO.toProject());
        // 공동 작업자 추가
        userList.forEach(u -> project.addCoworker(ProjectCoworker.builder()
                .user(User.builder().id(u.getUserId()).build())
                .project(project)
                .isOwner(false)
                .build()));
        // 소유자 추가
        project.addCoworker(ProjectCoworker.builder()
                .user(userRepository.findByUid(username).orElseThrow(() -> new IllegalArgumentException(username + " 작업자를 찾을 수 없습니다.")))
                .isOwner(true)
                .build());
        // 컬럼 및 태스크 추가
        postDTO.getColumns().forEach(columnDTO -> {
            ProjectColumn column = columnRepository.save(columnDTO.toEntity());
            column.setProject(project);
            log.info(column);
            // 태스크 추가
            columnDTO.getTasks().forEach(taskDTO -> {
                ProjectTask task = taskRepository.save(taskDTO.toProjectTask());
                task.setColumn(column);
                log.info(task);
                // 서브태스크 추가
                log.info("Subtasks : " + taskDTO.getSubTasks());
                if (taskDTO.getSubTasks()!=null&&!taskDTO.getSubTasks().isEmpty()) {
                    taskDTO.getSubTasks().forEach(subTaskDTO -> {
                        ProjectSubTask subTask = subTaskRepository.save(subTaskDTO.toEntity());
                        task.addSubTask(subTask); // 서브태스크 추가
                        log.info(subTaskDTO);
                    });
                }
                // 댓글 추가
                log.info("Comments : " + taskDTO.getComments());
                if (taskDTO.getComments()!=null&&!taskDTO.getComments().isEmpty()) {
                    taskDTO.getComments().forEach(commentDTO -> {
                        ProjectComment comment = commentRepository.save(commentDTO.toEntity());
                        task.addComment(comment); // 댓글 추가
                        log.info(commentDTO);
                    });
                }
                column.addTask(task); // 컬럼에 태스크 추가
            });
            project.addColumn(column); // 프로젝트에 컬럼 추가
        });
        return projectRepository.saveAndFlush(project);  // 최종적으로 프로젝트를 저장
    }



    public Map<String,Object> getAllProjects(String username) {
        Map<String,Object> map = new HashMap<>();

        List<ProjectCoworker> allProjects
                = coworkerRepository.findByUserAndProjectStatusIsNot(
                        userRepository.findByUid(username)
                            .orElseThrow(
                                    () -> new IllegalArgumentException(username+"작업자를 찾을 수 없습니다.")
                            ), 0);

        Map<String, List<ProjectCoworker>> groupedByStatus = allProjects.stream()
                .collect(Collectors.groupingBy(pc -> {
                    int status = pc.getProject().getStatus();
                    if (status == 1) return "waiting";
                    else if (status == 2) return "inProgress";
                    else if (status == 3) return "completed";
                    else return "unknown";
                }));

        map.put("waiting", groupedByStatus.getOrDefault("waiting", Collections.emptyList()).stream().map(ProjectCoworker::toGetProjectListDTO).collect(Collectors.toSet()));
        map.put("inProgress", groupedByStatus.getOrDefault("inProgress", Collections.emptyList()).stream().map(ProjectCoworker::toGetProjectListDTO).collect(Collectors.toSet()));
        map.put("completed", groupedByStatus.getOrDefault("completed", Collections.emptyList()).stream().map(ProjectCoworker::toGetProjectListDTO).collect(Collectors.toSet()));
        map.put("count",allProjects.size());
        return map;
    }

    public GetProjectDTO getProject(Long projectId) {
        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isPresent()) {
            Project project = optProject.get();
            return project.toGetProjectDTO();
        }
        return null;
    }
    public GetProjectDTO putProject(GetProjectDTO dto) {
        Optional<Project> optProject = projectRepository.findById(dto.getId());
        if (optProject.isPresent()) {
            Project project = optProject.get();
            project.updateProject(dto);
            return projectRepository.save(project).toGetProjectDTO();
        }
        return null;
    }
    public List<GetProjectColumnDTO> getColumns(Long projectId) {
        List<ProjectColumn> columnList = columnRepository.findAllByProjectId(projectId);
        return columnList.stream().map(ProjectColumn::toGetProjectColumnDTO).collect(Collectors.toList());
    }

    public ResponseEntity<?> getLeaderInfo(String group, String company) {
        Optional<Group> optGroup = groupRepository.findByNameAndStatusIsNotAndCompany(group,0,company);
        if(optGroup.isEmpty()) {
            return ResponseEntity.badRequest().body("정보가 일치하지 않습니다...");
        }
        User leader = optGroup.get().getGroupLeader().getUser();
        List<Project> project = projectRepository.findAllByCoworkers_UserAndStatusIsNot(leader,0);
        GetProjectLeaderDto projectLeaderDto = GetProjectLeaderDto.builder()
                .email(leader.getEmail())
                .name(leader.getName())
                .level(leader.selectLevelString())
                .id(leader.getId())
                .build();

        if(project.isEmpty()){
            projectLeaderDto.setTitle("없음");
            projectLeaderDto.setType("없음");
            projectLeaderDto.setStatus("없음");
        } else {
            if (project.size() >= 2) {
                projectLeaderDto.setTitle(project.get(0).getTitle() + " 외 " + (project.size()-1) + "개");
            } else {
                projectLeaderDto.setTitle(project.get(0).getTitle());
            }
            projectLeaderDto.setType(project.get(0).selectType());
            projectLeaderDto.setStatus(project.get(0).selectStatus());
        }
        return ResponseEntity.ok(projectLeaderDto);
    }

    public GetProjectColumnDTO addColumn(GetProjectColumnDTO columnDTO, Long projectId) {
        ProjectColumn col = columnDTO.toEntityAddProject(projectId);
        Project pj = projectRepository.findById(projectId).orElseThrow();
        pj.addColumn(col);
        return col.toGetProjectColumnDTO();
    }

    public GetProjectTaskDTO addTask(GetProjectTaskDTO taskDTO) {
        log.info("saveTask 0 : " + taskDTO);
        ProjectTask task = taskDTO.toProjectTask();
        log.info("saveTask 1 : " + task);
        task.setAssign(task.getAssign().stream().map(assignRepository::save).collect(Collectors.toList()));
        ProjectColumn col = columnRepository.findById(task.getColumn().getId())
                .orElseThrow(() -> new RuntimeException("Column not found"));
        col.addTask(task);
        task = taskRepository.save(task);  // 새 task 저장
        columnRepository.save(col);

        log.info("saveTask 2 : " + task);
        log.info("saveTask 3 : " + col);
        return task.toGetProjectTaskDTO();
    }

    public GetProjectTaskDTO updateTask(GetProjectTaskDTO taskDTO) {
        ProjectTask task = taskDTO.toProjectTask();
        ProjectTask originTask = taskRepository.findById(task.getId()).orElseThrow();
        // 기존 Assign 데이터 삭제
        assignRepository.deleteByTaskId(originTask.getId());

        // Column이 영속성 컨텍스트에 있는지 확인
        if (task.getColumn() != null && task.getColumn().getId() != null) {
            ProjectColumn column = columnRepository.findById(task.getColumn().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + task.getColumn().getId()));
            task.setColumn(column); // 영속화된 Column을 설정
        }
        // Task 정보 업데이트
        originTask.update(task);

        return originTask.toGetProjectTaskDTO();
    }

    public void updateCoworkers(PatchCoworkersDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없습니다."));

        if (dto.getRemovedCoworkers() != null) {
            dto.getRemovedCoworkers().forEach(userId -> {
                project.getCoworkers().stream()
                        .filter(coworker -> coworker.getUser().getId().equals(userId))
                        .findFirst()
                        .ifPresent(project::removeCoworker);
            });
        }
        if (dto.getAddedCoworkers() != null) {
            dto.getAddedCoworkers().forEach(userId -> {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("작업자를 찾을 수 없습니다. ID: " + userId));
                project.addCoworker(ProjectCoworker.builder()
                        .user(user)
                        .project(project)
                        .isOwner(false)
                        .build());
            });
        }
    }
    public GetProjectSubTaskDTO insertSubTask(GetProjectSubTaskDTO dto){
        ProjectSubTask entity = dto.toEntity();
        ProjectTask task = taskRepository.findById(dto.getTaskId()).orElseThrow();
        task.addSubTask(entity);
        return entity.toDTO();
    }
    public GetProjectSubTaskDTO clickSubTask(Long id){
        ProjectSubTask subTask = subTaskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("작업자를 찾을 수 없습니다. ID: " + id));
        subTask.click();
        return subTask.toDTO();
    }

    public void delete(String type, Long id) {
        try {
            switch (type) {
                case "subtask" -> subTaskRepository.deleteById(id);
                case "comment" -> commentRepository.deleteById(id);
                case "task" -> taskRepository.deleteById(id);
                case "column" -> columnRepository.deleteById(id);
                case "project" -> projectRepository.deleteById(id);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public ResponseEntity<?> getProjects(String company, String group) {
        Optional<Group> optGroup = groupRepository.findByNameAndStatusIsNotAndCompany(group,0,company);
        if(optGroup.isEmpty()) {
            return ResponseEntity.badRequest().body("정보가 일치하지 않습니다...");
        }

        List<GroupMapper> groupMappers = optGroup.get().getGroupMappers();
        List<User> users = groupMappers.stream().map(GroupMapper::getUser).toList();
        List<Project> projects = new ArrayList<>();
        for (User user : users) {
            List<Project> project = projectRepository.findAllByCoworkers_UserAndStatusIsNot(user,0);
            projects.addAll(project);
        }
        Set<Project> uniqueProjects = new HashSet<>(projects);
        projects = new ArrayList<>(uniqueProjects);

        List<GetProjects> dtos = projects.stream().map(Project::toGetProjects).toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getAdminProjectColumns(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            return ResponseEntity.badRequest().body("프로젝트가 없습니다...");
        }
//        Set<ProjectColumn> columns = project.get().getColumns();

        System.out.println("============================222");
        return ResponseEntity.ok("columns");
    }

    public GetProjectCommentDTO addComment(GetProjectCommentDTO dto) {
        ProjectTask task = taskRepository.findById(dto.getTaskId()).orElseThrow();
        log.info("addComment 0 : " + task);
        ProjectComment comment = ProjectComment.builder()
                .task(task)
                .user(dto.toEntity().getUser())
                .content(dto.getContent())
                .build();
        log.info("addComment 1 : " + comment);
        ProjectComment savedComment = commentRepository.save(comment);
        log.info("addComment 2 : " + savedComment);
        return savedComment.toDTO();
    }

    public List<ReqHomeProjectDTO> getHomeProject(Long userId) {
        Map<String,Object> map = new HashMap<>();

        List<ProjectCoworker> allProjects
                = coworkerRepository.findByUserAndProjectStatusIsNot(
                        userRepository.findById(userId)
                        .orElseThrow(
                                () -> new IllegalArgumentException("작업자를 찾을 수 없습니다.")
                        ), 0);

        List<ReqHomeProjectDTO> dtos = allProjects.stream().limit(3).map(v -> {
            List<GetProjectColumnDTO> columnDTOS = v.getProject().getColumns().stream().map(c -> {
                List<GetProjectTaskDTO> taskDTOS = c.getTasks().stream().map(t -> {
                                                            return GetProjectTaskDTO.builder()
                                                                    .title(t.getTitle())
                                                                    .content(t.getContent())
                                                                    .priority(t.getPriority())
                                                                    .status(t.getStatus())
                                                                    .duedate(t.getDuedate())
                                                                    .build();
                                                        }).toList();
                                                    return GetProjectColumnDTO.builder()
                                                            .title(c.getTitle())
                                                            .color(c.getColor())
                                                            .id(c.getId())
                                                            .tasks(taskDTOS)
                                                            .build();
                                                }).toList();

            return ReqHomeProjectDTO.builder()
                    .projectId(v.getProject().getId())
                    .projectName(v.getProject().getTitle())
                    .getProjectColumn(columnDTOS)

                    .build();
        }).toList();

        return dtos;
    }
}
