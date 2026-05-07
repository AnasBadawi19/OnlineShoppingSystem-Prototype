import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

class Product {
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void reduceStock(int quantity) { stock -= quantity; }

    public String toString() {
        return name + " ($" + price + ", Stock: " + stock + ")";
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    public int hashCode() {
        return Objects.hash(username);
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return product.getPrice() * quantity; }
}

class Order {
    private int orderId;
    private User user;
    private List<CartItem> items;
    private double total;
    private String message;

    public Order(int orderId, User user, List<CartItem> items) {
        this.orderId = orderId;
        this.user = user;
        this.items = new ArrayList<>(items);
        this.total = calculateTotal();
        this.message = "Thank you for shopping at our online store!";
    }

    private double calculateTotal() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }

    public String toString() {
        return "Order ID: " + orderId + "\nUser: " + user.getUsername() + "\nTotal: $" + total + "\n" + message;
    }
}

class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}

class GuiFrame extends JFrame {
    private List<Product> products = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private ShoppingCart cart = new ShoppingCart();
    private User currentUser;
    private int nextOrderId = 0;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private JTextField usernameField = new JTextField(15);
    private JTextField passwordField = new JTextField(8);
    private JTextField[] quantityTextFields;
    private JButton[] productButtons;

    public GuiFrame() {
        super("Online Shopping System");
        initializeProducts();
        initializeUI();
    }

    private void initializeProducts() {
        products.add(new Product("Laptop", 1000.0, 10));
        products.add(new Product("Smartphone", 500.0, 20));
        products.add(new Product("Headphones", 100.0, 15));
        products.add(new Product("Keyboard", 50.0, 25));
    }

    private void initializeUI() {
        JPanel loginPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        loginPanel.add(new JLabel("Welcome to our online store!"));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        mainPanel.add(loginPanel, "login");

        JPanel productListPanel = new JPanel(new GridLayout(products.size() + 4, 1, 5, 5));

        productButtons = new JButton[products.size()];
        quantityTextFields = new JTextField[products.size()];

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            JPanel row = new JPanel(new GridLayout(1, 3, 5, 5));
            productButtons[i] = new JButton(product.toString());
            quantityTextFields[i] = new JTextField("1", 3);
            row.add(productButtons[i]);
            row.add(new JLabel("Quantity:"));
            row.add(quantityTextFields[i]);
            productListPanel.add(row);
        }

        JButton addToCartButton = new JButton("Add Selected to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton placeOrderButton = new JButton("Place Order");
        JButton logoutButton = new JButton("Logout");

        productListPanel.add(addToCartButton);
        productListPanel.add(viewCartButton);
        productListPanel.add(placeOrderButton);
        productListPanel.add(logoutButton);

        mainPanel.add(productListPanel, "products");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        for (int i = 0; i < products.size(); i++) {
            final int index = i;
            productButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectProduct(index);
                }
            });
        }

        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        placeOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!");
            return;
        }

        currentUser = new User(username, password);
        if (!users.contains(currentUser)) {
            users.add(currentUser);
        }

        JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username);
        cardLayout.show(mainPanel, "products");
    }

    private void selectProduct(int index) {
        Product selectedProduct = products.get(index);
        JOptionPane.showMessageDialog(this, 
            "Product: " + selectedProduct.getName() + 
            "\nPrice: $" + selectedProduct.getPrice() + 
            "\nStock: " + selectedProduct.getStock() + " units available");
    }

    private void addToCart() {
        boolean itemAdded = false;
        for (int i = 0; i < products.size(); i++) {
            Product selectedProduct = products.get(i);
            int quantity;
            try {
                quantity = Integer.parseInt(quantityTextFields[i].getText());
                if (quantity < 1) {
                    continue;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity for " + selectedProduct.getName());
                return;
            }
            if (quantity > selectedProduct.getStock()) {
                JOptionPane.showMessageDialog(this, "Not enough stock for " + selectedProduct.getName() + 
                    "\nAvailable: " + selectedProduct.getStock() + " units");
                return;
            }
            cart.addItem(selectedProduct, quantity);
            JOptionPane.showMessageDialog(this, selectedProduct.getName() + " x" + quantity + " added to cart!");
            itemAdded = true;
            quantityTextFields[i].setText("1");
        }
        if (!itemAdded) {
            JOptionPane.showMessageDialog(this, "Please set quantity greater than 0 for items you want to add!");
        }
    }

    private void viewCart() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        String cartContents = "Cart Contents:\n\n";
        double totalPrice = 0;
        
        for (CartItem item : cart.getItems()) {
            cartContents = cartContents + "• " + item.getProduct().getName() + 
                " x " + item.getQuantity() + 
                " = $" + item.getTotalPrice() + "\n";
            totalPrice += item.getTotalPrice();
        }
        
        cartContents = cartContents + "\nTotal: $" + totalPrice;
        JOptionPane.showMessageDialog(this, cartContents);
    }

    private void placeOrder() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        double total = 0;
        String orderSummary = "Order Summary:\n\n";
        
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceStock(item.getQuantity());
            orderSummary = orderSummary + "• " + item.getProduct().getName() + 
                " x " + item.getQuantity() + 
                " = $" + item.getTotalPrice() + "\n";
            total += item.getTotalPrice();
        }
        
        Order order = new Order(nextOrderId++, currentUser, cart.getItems());
        orders.add(order);
        cart.clear();
        
        orderSummary = orderSummary + "\nTotal: $" + total + 
            "\nOrder ID: " + (nextOrderId - 1) + 
            "\n\nThank you for shopping with us!";
        
        JOptionPane.showMessageDialog(this, orderSummary);
    }

    private void logout() {
        usernameField.setText("");
        passwordField.setText("");
        cart.clear();
        currentUser = null;
        cardLayout.show(mainPanel, "login");
        JOptionPane.showMessageDialog(this, "You have been logged out successfully!");
    }
}

public class OnlineShoppingSystem {
    public static void main(String[] args) {
        GuiFrame f = new GuiFrame();
        f.setSize(400, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}