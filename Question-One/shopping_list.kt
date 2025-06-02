
fun main(){
    val shoppingList = ShoppingList()

    shoppingList.addItem("Milk", 1.99, 2)
    shoppingList.addItem("Bread", 2.49)
    shoppingList.addItem("Eggs", 3.99, 12)
    shoppingList.removeItem("eggs")
    println(shoppingList.totalCost())
}


data class Item(
    val name: String,
    val price: Double,
    var quantity: Int = 1
)

class ShoppingList {
    val items = mutableListOf<Item>()

    fun addItem(name: String, price: Double, quantity: Int = 1) {
        val existingItem = items.find { it.name.equals(name, ignoreCase = true) }
        if (existingItem != null) {
            existingItem.quantity += quantity
            println("Updated ${existingItem.name} quantity to ${existingItem.quantity}.")
        } else {
            items.add(Item(name, price, quantity))
            println("Added ${name} to the shopping list.")
        }
    }

    fun removeItem(name: String) {
        val removed = items.removeIf { it.name.equals(name, ignoreCase = true) }
        if (removed) {
            println("Removed '$name' from the shopping list.")
        } else {
            println("Item '$name' not found.")
        }
    }

    fun totalCost(): Double {
        return items.sumOf { it.price * it.quantity }
    }
}
