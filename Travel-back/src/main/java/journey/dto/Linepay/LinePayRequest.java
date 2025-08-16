package journey.dto.Linepay;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinePayRequest {

    private String productName;
    private int amount;
    private String currency;
    private String orderId;
    private List<Package> packages;
    private RedirectUrls redirectUrls;

    public LinePayRequest() {
    }

    public LinePayRequest(String productName, int amount, String currency, String orderId,
            List<Package> packages,
            RedirectUrls redirectUrls) {
        this.productName = productName;
        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
        this.packages = packages;
        this.redirectUrls = redirectUrls;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public RedirectUrls getRedirectUrls() {
        return redirectUrls;
    }

    public void setRedirectUrls(RedirectUrls redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public static class Package {
        private String id;
        private String name;
        private int amount;

        private List<Product> products;

        public Package() {
            this.products = new ArrayList<>();
        }

        public Package(String id, String name, int amount, List<Product> products) {
            this.id = id;
            this.name = name;
            this.amount = amount;
            this.products = products;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    // Inner classes for nested objects
    public static class Product {
        private String id;
        private String name;
        private int quantity;
        private int price;
        private String imageUrl;

        public Product() {
        }

        public Product(String name, int quantity, int price, String imageUrl) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public static class RedirectUrls {
        @JsonProperty("confirmUrl")
        private String confirmUrl; // LINE Pay 成功回調的後端 URL
        @JsonProperty("cancelUrl")
        private String cancelUrl; // LINE Pay 取消回調的前端 URL

        public RedirectUrls() {
        }

        public RedirectUrls(String confirmUrl, String cancelUrl) {
            this.confirmUrl = confirmUrl;
            this.cancelUrl = cancelUrl;
        }

        // Getters, Setters
        public String getConfirmUrl() {
            return confirmUrl;
        }

        public void setConfirmUrl(String confirmUrl) {
            this.confirmUrl = confirmUrl;
        }

        public String getCancelUrl() {
            return cancelUrl;
        }

        public void setCancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
        }
    }
}
