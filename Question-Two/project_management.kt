
data class Task(
    val name: String,
    val assignedTo: String,
    val deadline: String,
    val dependencies: List<String> = listOf(),
    var status: String = "NOT_STARTED"
)
class Project(val name: String) {
    private val tasks = mutableListOf<Task>()
    fun addTask(task: Task) {
        tasks.add(task)
    }
    fun report() {
        println("Report for project: $name")
        for (task in tasks) {
            println("- ${task.name} [${task.status}] (Assigned to: ${task.assignedTo}, Deadline: ${task.deadline})")
        }
    }
}

fun main() {
    val p = Project("Website Redesign")
    val t1 = Task("Design mockup", "Kisanet", "2025-06-01")
    val t2 = Task("Frontend dev", "Lwam", "2025-06-10", dependencies = listOf("Design mockup"))
    p.addTask(t1)
    p.addTask(t2)
    p.report()
}
