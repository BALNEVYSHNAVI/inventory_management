import java.util.*;

class Inventoryinfo
{
    /** An item contains details like id,name,category and quantity **/
    String id, name, category;
    int quantity;

    public Inventoryinfo(String id, String name, String category, int quantity)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        return "ID: " + id + ", Name: " + name + ", Category: " + category + ", Quantity: " + quantity;
    }
}

class Inventorysystem
{
    private final Map<String, Inventoryinfo> inventory = new HashMap<>();
    private final Map<String, List<Inventoryinfo>> categoryinfo = new HashMap<>();
    private final Set<String> categories = new HashSet<>();
    private final List<Inventoryinfo> restockList = new ArrayList<>();
    private final int threshold;

    public Inventorysystem(int threshold)
    {
        this.threshold = threshold;
    }
    /** method to add item to the inventory **/
    public void add(String id, String name, String category, int quantity)
    {
        /** checking if any field is empty or not **/
        if (id.isEmpty() || name.isEmpty() || category.isEmpty() || quantity < 0)
        {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        categories.add(category);

        Inventoryinfo item = inventory.get(id);
        if (item != null)
        {
            item.setQuantity(item.getQuantity() + quantity);
            updatecategory(item);
        }
        else
        {
            item = new Inventoryinfo(id, name, category, quantity);
            inventory.put(id, item);
            categoryinfo.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
        }
        System.out.println("Added successfully");
        /** checking for threshold value **/
        if (item.getQuantity() < threshold)
        {
            System.out.println("Restock Alert: " + name + " (ID: " + id + ") is below threshold!");
        }
        updateRestockList(item);
    }
    /** updating quantity of existing item **/
    public void update(String id, int quantity)
    {
        Inventoryinfo item = inventory.get(id);
        if (item != null)
        {
            item.setQuantity(quantity);
            updatecategory(item);
            updateRestockList(item);
            System.out.println("Updated successfully");
            /** checking for threshold value **/
            if (item.getQuantity() < threshold)
            {
                System.out.println("Restock Alert: " + item.getName() + " (ID: " + id + ") is below threshold!");
            }
        }
        else
        {
            System.out.println("Item not found.");
        }
    }
    /** removing item from inventory **/
    public void remove(String id)
    {
        Inventoryinfo item = inventory.remove(id);
        if (item != null)
        {
            categoryinfo.get(item.getCategory()).remove(item);
            if (categoryinfo.get(item.getCategory()).isEmpty())
            {
                categoryinfo.remove(item.getCategory());
                categories.remove(item.getCategory());
            }
            restockList.remove(item);
            System.out.println("Removed successfully");
        }
        else
        {
            System.out.println("Item not found.");
        }
    }
    /** displaying all items in inventory irrespective of category **/
    public void displayInventory()
    {
        if (inventory.isEmpty())
        {
            System.out.println("No items in the inventory.");
        }
        else
        {
            inventory.values().forEach(System.out::println);
        }
    }
    /** method for knowing items category wise **/
    public void viewbycategory(String category)
    {
        List<Inventoryinfo> items = categoryinfo.get(category);
        if (items == null || items.isEmpty())
        {
            System.out.println("No items in this category.");
        }
        else
        {
            items.forEach(System.out::println);
        }
    }

    public List<String> getCategories()
    {
        return new ArrayList<>(categories);
    }
    /** method that gives top k items of all categories **/
    public List<Inventoryinfo> getTopKItems(int k)
    {
        List<Inventoryinfo> topItems = new ArrayList<>(inventory.values());
        topItems.sort((a, b) -> b.getQuantity() - a.getQuantity());

        List<Inventoryinfo> result = new ArrayList<>();
        int count = 0;
        for (Inventoryinfo item : topItems)
        {
            if (count >= k) break;
            result.add(item);
            count++;
        }
        return result;
    }

    /** method that shows items which are available for restocking **/
    public void viewRestockList()
    {
        if (restockList.isEmpty())
        {
            System.out.println("No items need restocking.");
        }
        else
        {
            System.out.println("Items needing restocking:");
            restockList.forEach(System.out::println);
        }
    }

    private void updateRestockList(Inventoryinfo item)
    {
        if (item.getQuantity() < threshold && !restockList.contains(item))
        {
            restockList.add(item);
        }
        else if (item.getQuantity() >= threshold)
        {
            restockList.remove(item);
        }
    }

    private void updatecategory(Inventoryinfo item)
    {
        categoryinfo.get(item.getCategory()).sort((a, b) -> b.getQuantity() - a.getQuantity());
    }
}
/** Main system begins here**/
public class inventory
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        Inventorysystem obj = new Inventorysystem(8);
        /** menu based options**/
        while (true)
        {
            System.out.println("\nInventory Management System:");
            System.out.println("1. Add Item");
            System.out.println("2. Update Item Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. View Items by Category");
            System.out.println("5. View Top K Items");
            System.out.println("6. Display All Items");
            System.out.println("7. View Restock List");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice)
            {
                case 1:
                    System.out.print("Enter ID: ");
                    String id1 = sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter Quantity: ");
                    int quantity = sc.nextInt();
                    obj.add(id1, name, category, quantity);
                    break;

                case 2:
                    System.out.print("Enter ID to Update: ");
                    id1 = sc.nextLine();
                    System.out.print("Enter new Quantity: ");
                    quantity = sc.nextInt();
                    obj.update(id1, quantity);
                    break;

                case 3:
                    System.out.print("Enter ID to Remove: ");
                    id1 = sc.nextLine();
                    obj.remove(id1);
                    break;

                case 4:
                    System.out.println("\nAvailable Categories:");
                    List<String> categories = obj.getCategories();
                    if (categories.isEmpty())
                    {
                        System.out.println("No categories found.");
                    }
                    else
                    {
                        for (int i = 0; i < categories.size(); i++)
                        {
                            System.out.printf("%d. %s%n", i + 1, categories.get(i));
                        }
                        System.out.print("Select a category: ");
                        int cchoice = sc.nextInt();
                        sc.nextLine();
                        if (cchoice < 1 || cchoice > categories.size())
                        {
                            System.out.println("Invalid choice.");
                        }
                        else
                        {
                            obj.viewbycategory(categories.get(cchoice - 1));
                        }
                    }
                    break;

                case 5:
                    System.out.print("Enter value of K: ");
                    int k = sc.nextInt();
                    List<Inventoryinfo> topItems = obj.getTopKItems(k);
                    if (topItems.isEmpty())
                    {
                        System.out.println("No items in the inventory.");
                    }
                    else
                    {
                        topItems.forEach(System.out::println);
                    }
                    break;

                case 6:
                    obj.displayInventory();
                    break;

                case 7:
                    obj.viewRestockList();
                    break;

                case 8:
                    System.out.println("Exited");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
