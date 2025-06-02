def add_item(shopping_list,name,price,quantity=1):
    for item in shopping_list:
        if item['name'].lower()==name.lower():  
            item['quantity']+=quantity
            print(f"Updated {item['name'] } quantity to {item['quantity']}.")
            return 
            shopping_list.append({'name',name,'price',price,'quantity',quantity})
            print(f'added {name} to the shopping list.')
def remove_item(shopping_list,name):
    for item in shopping_list:
        if item['name'].lower()==name.lower():
            shopping_list.remove(item)
            print(f"Removed '{name}' from the shopping list.")
            return
        print(f"item '{name}' not found in the shopping list.")

def total_cost(shopping_list):
    return sum(item['price']*item['quantity'] for item in shopping_list)