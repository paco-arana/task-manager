package com.paco.demo;

public class Task {
 public int id;
 public boolean completed;
 public String text;

 // Priority is saved as int, 1 is high priority, 3 is low priority
 public int priority = 0;

 // dueDates are saved as an int in yyyymmdd format for simplifying the sorting
 public int dueDate = 0;

 // Dates are saved as seconds (not milliseconds) since Unix epoch
 // "time to finish task" can be easily calculater by "endDate - startDate"
 public int startDate = 0;
 public int endDate = 0;

 public void setId(int id) {
     this.id = id;
 }

 public int getId() {
     return id;
 }

 public void setText(String text) {
     this.text = text;
 }

 public String getText() {
     return text;
 }

 public void setPriority(int priority) {
     this.priority = priority;
 }

 public int getPriority() {
     return priority;
 }

 public boolean isCompleted() {
     return completed;
 }
}
