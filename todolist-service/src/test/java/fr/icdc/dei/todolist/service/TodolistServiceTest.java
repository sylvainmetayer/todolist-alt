package fr.icdc.dei.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.icdc.dei.todolist.persistence.entity.Task;
import fr.icdc.dei.todolist.persistence.entity.TaskStatus;
import fr.icdc.dei.todolist.persistence.entity.User;
import fr.icdc.dei.todolist.service.enums.TaskStatusEnum;

public class TodolistServiceTest extends AbstractServiceTest {

	private static final long TASK_TO_AFFECT_ID = 1L;
	private static final long DELEGATED_TASK_ID = 2L;
	private static final long USER_ID = 1L;
	private static final long DELEGATE_USER_ID = 2L;

	private static final long DATE_ENDING_LESS_THAN_A_WEEK =4L;
	private static final long DATE_ENDING_MORE_THAN_A_WEEK = 2L;

	private static User user = new User();
	private static Task task = new Task();
	private static Task taskDateEndingLessThanAWeek = new Task();
	private static Task taskDateEndingMoreThanAWeek = new Task();

	@Autowired
	private TodolistService todolistService;

	@BeforeClass
	public static void setUp() {
		user.setId(USER_ID);
		task.setId(TASK_TO_AFFECT_ID);
		taskDateEndingLessThanAWeek.setId(DATE_ENDING_LESS_THAN_A_WEEK);
		taskDateEndingLessThanAWeek.setStatus(new TaskStatus(TaskStatusEnum.STARTED.getValue()));
		taskDateEndingMoreThanAWeek.setId(DATE_ENDING_MORE_THAN_A_WEEK);
	}

	@Test
	public void testArchiveFinishedTasksSinceOneMonth() {
		assertTrue(CollectionUtils.isNotEmpty(todolistService.archiveTasks()));
	}

	@Test
	public void testAffectTaskToAnotherUser() {
		assertEquals(todolistService.affectTaskToUser(task.getId(), user.getId()).getTaskOwners().size(), 2);
	}

	@Test
	public void testAffectTaskToAnotherUserGotPendingStatus() {
		assertEquals(todolistService.affectTaskToUser(task.getId(), user.getId()).getStatus().getId(),
				TaskStatusEnum.DELEGATION_PENDING.getValue());
	}

	@Test
	public void testAcceptDelegatedTask() {
		assertEquals(todolistService.acceptDelegatedTask(DELEGATED_TASK_ID, DELEGATE_USER_ID).getStatus().getId(),
				TaskStatusEnum.DELEGATED.getValue());
	}

	@Test
	public void testAddTaskWorks() {
		assertTrue(todolistService.addTask("taskName", 1).getId() > 0);
	}

	@Test
	public void testListTaskStatus() {
		assertTrue(CollectionUtils.isNotEmpty(todolistService.listTaskStatus()));
	}

	@Test
	public void testFinishTaskWork() {
		assertEquals(todolistService.finishTask(taskDateEndingMoreThanAWeek.getId()).getStatus().getId(),
				TaskStatusEnum.FINISHED.getValue());
	}
	
	@Test
	public void testTaskFinishedLessThanAWeekStaysTheSame() {
		assertEquals(todolistService.finishTask(taskDateEndingLessThanAWeek.getId()).getStatus().getId(),
				taskDateEndingLessThanAWeek.getStatus().getId());
	}

}
