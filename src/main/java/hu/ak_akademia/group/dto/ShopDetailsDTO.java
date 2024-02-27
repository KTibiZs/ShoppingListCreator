package hu.ak_akademia.group.dto;

import java.util.List;

public class ShopDetailsDTO {
    private String name;
    private List<ItemDetailsDTO> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemDetailsDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDetailsDTO> items) {
        this.items = items;
    }
}
