package hu.ak_akademia.group.service;

import hu.ak_akademia.group.dto.ItemDetailsDTO;
import hu.ak_akademia.group.dto.ShopDetailsDTO;
import hu.ak_akademia.group.dto.ShopNameDTO;
import hu.ak_akademia.group.model.Item;
import hu.ak_akademia.group.model.Shop;
import hu.ak_akademia.group.repository.ItemRepository;
import hu.ak_akademia.group.repository.ShopRepository;
import hu.ak_akademia.group.service.exception.ItemNotFoundException;
import hu.ak_akademia.group.service.exception.ShopNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    // Dependency injection
    // adattag
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private ShopRepository shopRepository;

    // konstruktor
    private ItemRepository itemRepository;
    private ShopRepository shopRepository;

    public MainService(ItemRepository itemRepository, ShopRepository shopRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
    }

    public void saveShop(Shop shop) {
        shopRepository.save(shop);
    }

    public boolean hasSameShop(String shopName) {
        List<Shop> all = shopRepository.findAll();
        for (Shop shop : all) {
            if (shop.getName().equalsIgnoreCase(shopName)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Shop> loadShopWithItems(ShopNameDTO shopNameDTO) {
        return (shopNameDTO == null) ? Optional.empty() : shopRepository.findByNameWithItems(shopNameDTO.getName());
    }

    public ShopDetailsDTO mapShopToShopDetailsDTO(Shop shop) {
        ShopDetailsDTO shopDetailsDTO = new ShopDetailsDTO();
        shopDetailsDTO.setName(shop.getName());
        List<Item> items = shop.getItems();
        List<ItemDetailsDTO> itemDetailsDTOS = new ArrayList<>();
        for (Item item : items) {
            ItemDetailsDTO itemDetailsDTO = new ItemDetailsDTO();
            itemDetailsDTO.setId(item.getId());
            itemDetailsDTO.setName(item.getName());
            itemDetailsDTO.setQuantity(item.getQuantity());
            itemDetailsDTO.setQtyUnit(item.getQtyUnit());
            itemDetailsDTOS.add(itemDetailsDTO);
        }
        shopDetailsDTO.setItems(itemDetailsDTOS);
        return shopDetailsDTO;
    }

    public boolean itemToBeAddIsEmpty(Item item) {
        return item.getName().equals("") || item.getQuantity() == null || item.getQtyUnit().equals("");
    }

    public void addItem(ShopNameDTO shopNameDTO, Item item) {
        if (shopNameDTO == null) {
            throw new ShopNotFoundException("N/A");
        }
        Optional<Shop> optShop = shopRepository.findByNameWithItems(shopNameDTO.getName());
        if (optShop.isPresent()) {
            Shop shop = optShop.get();
            shop.addItem(item);
            shopRepository.save(shop);
        } else {
            throw new ShopNotFoundException(shopNameDTO.getName());
        }
    }

    public List<Shop> getShops() {
        return shopRepository.findAll();
    }

    public Optional<Shop> loadShop(String shopName) {
        return shopRepository.findByName(shopName);
    }

    public void removeShop(int id) {
        shopRepository.deleteById(id);
    }

    public void removeItem(ShopNameDTO shopNameDTO, int itemId) {
        if (shopNameDTO == null) {
            throw new ShopNotFoundException("N/A");
        }
        itemRepository.deleteById(itemId);
    }

    public Optional<Item> getItem(int id) {
        return itemRepository.findById(id);
    }

    public void update(ShopNameDTO shopNameDTO, Item item) {
        if (shopNameDTO == null) {
            throw new ShopNotFoundException("N/A");
        }
        Optional<Item> optDbItem = itemRepository.findById(item.getId());
        if (optDbItem.isPresent()) {
            Item dbItem = optDbItem.get();
            dbItem.setName(item.getName());
            dbItem.setQuantity(item.getQuantity());
            dbItem.setQtyUnit(item.getQtyUnit());
            itemRepository.save(dbItem);
        } else {
            throw new ItemNotFoundException(item.getId());
        }
    }
}
