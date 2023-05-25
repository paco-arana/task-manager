package com.paco.demo;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	private List<Task> taskList=new ArrayList<Task>();

    public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
    }

    @PostMapping("/create")
    public int create(@RequestBody() Task task) {
		task.id = taskList.size() + 1;
		taskList.add(task);

    	return task.id;
    }

	@GetMapping("/todos")
    public List<Task> getAllTodos() {
    	return taskList;
    }

	@PutMapping("/todos/{id}")
    public Task updateTodo(@PathVariable int id, @RequestBody() Task task) throws Exception {

		Task taskFound = taskList.stream().filter(t -> {
			return t.id == id;
		}).findAny().orElse(null);

		if (taskFound == null) {
			throw new Exception("No task with matching id");
		}

		taskFound.text = task.text;
		
    	return taskFound;
    }

	@DeleteMapping("/todos/{id}")
	public List<Task> deleteTodo(@PathVariable int id) {

		Task taskFound = taskList.stream().filter(t -> {
			return t.id == id;
		}).findAny().orElse(null);

		taskList.remove(taskFound);

		return taskList;		
	}


}