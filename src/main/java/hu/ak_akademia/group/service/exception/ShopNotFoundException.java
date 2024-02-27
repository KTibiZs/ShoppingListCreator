package hu.ak_akademia.group.service.exception;

public class ShopNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShopNotFoundException(String shopName) {
        super(shopName);
    }
}
