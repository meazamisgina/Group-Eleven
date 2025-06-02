class ShoppingList {
    constructor() {
      this.items = [];
    }
    addItem(name, price, quantity = 1) {
      const existingItem = this.items.find(item => item.name.toLowerCase() === name.toLowerCase());
      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        this.items.push({ name, price, quantity });
        
      }
    }
    removeItem(name) {
      this.items = this.items.filter(item => item.name.toLowerCase() !== name.toLowerCase());
      
    }
  
    
    totalCost() {
      return this.items.reduce((total, item) => total + item.price * item.quantity, 0);
    }
 
}
  
  const myList = new ShoppingList();
  
  myList.addItem("Banana", 1.99, 2);
  myList.addItem("Orange", 2.49);
  myList.addItem("Biscute", 3.99, 12);
  myList.removeItem("Apple");
  console.log(`Final total: $${myList.totalCost()}`);