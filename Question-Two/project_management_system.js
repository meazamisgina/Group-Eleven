class Task {
    constructor(name, assignedTo, deadline, dependencies = []) {
      this.name = name;
      this.assignedTo = assignedTo;
      this.deadline = deadline;
      this.status = "NOT_STARTED";
      this.dependencies = dependencies;
    }
  }
  class Project {
    constructor(name) {
      this.name = name;
      this.tasks = [];
    }
    addTask(task) {
      this.tasks.push(task);
    }
    report() {
      console.log(`Report for project: ${this.name}`);
      this.tasks.forEach(task => {
        console.log(`- ${task.name} [${task.status}] (Assigned to: ${task.assignedTo}, Deadline: ${task.deadline})`);
      });
    }
  }
  const p = new Project("Website Redesign");
  const t1 = new Task("Design mockup", "Lwam", "2025-06-01");
  const t2 = new Task("Frontend dev", "Hewan", "2025-06-10", ["Design mockup"]);
  p.addTask(t1);
  p.addTask(t2);
  p.report();
  
  