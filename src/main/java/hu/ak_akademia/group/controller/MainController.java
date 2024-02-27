package hu.ak_akademia.group.controller;

import hu.ak_akademia.group.dto.ShopDetailsDTO;
import hu.ak_akademia.group.dto.ShopNameDTO;
import hu.ak_akademia.group.model.Item;
import hu.ak_akademia.group.model.Shop;
import hu.ak_akademia.group.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MainController {

    private MainService mainService;
    private HttpServletRequest httpServletRequest;

    public MainController(MainService mainService, HttpServletRequest httpServletRequest) {
        this.mainService = mainService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("shop", new Shop("Aldi"));
        model.addAttribute("shops", mainService.getShops());
        return "index";  // ezt a template-t hívja meg
    }

    @GetMapping("/new_shop")
    public String newShop(Shop shop, HttpSession session) {
        ShopNameDTO shopNameDTO = new ShopNameDTO(shop.getName());
        session.setAttribute("shopName", shopNameDTO);
        if (!mainService.hasSameShop(shop.getName()) && !shop.getName().equals("")) {
            mainService.saveShop(shop);
        }
        return "redirect:/display";  // ezt az endpoint-ot hívja meg
    }

    @GetMapping("/display")
    public String display(Model model) {
        ShopNameDTO shopNameDTO = (ShopNameDTO) httpServletRequest.getSession().getAttribute("shopName");
        if (shopNameDTO == null) {
            return "redirect:/";
        }
        Optional<Shop> optShop = mainService.loadShopWithItems(shopNameDTO);
        if (optShop.isPresent()) {
            ShopDetailsDTO shopDetailsDTO = mainService.mapShopToShopDetailsDTO(optShop.get());
            model.addAttribute("shop", shopDetailsDTO);
            model.addAttribute("item", new Item("Bread", 1.5, "kg"));
            return "display";
        } else {
            return "redirect:/";
        }
    }

// localhost:8080/new_shop?name=Aldi&taste=fine // get lekérés

    @PostMapping("/add_item")
    public String addItem(Item item) {
        ShopNameDTO shopNameDTO = (ShopNameDTO) httpServletRequest.getSession().getAttribute("shopName");
        if (shopNameDTO == null) {
            return "redirect:/";
        }
        if (!mainService.itemToBeAddIsEmpty(item)) {
            mainService.addItem(shopNameDTO, item);
        }
        return "redirect:/display";
    }

    @GetMapping("/load_shop")
    public String loadShop(String name, HttpSession session) {
        Optional<Shop> optShop = mainService.loadShop(name);
        ShopNameDTO shopNameDTO = new ShopNameDTO(name);
        if (optShop.isPresent()) {
            session.setAttribute("shopName", shopNameDTO);
            return "redirect:/display";
        }
        return "index";
    }

    @GetMapping("/remove_shop")
    public String removeShop(int id) {
        mainService.removeShop(id);
        return "redirect:/";
    }

    @GetMapping("/remove_item")
    public String removeItem(int id) {
        ShopNameDTO shopNameDTO = (ShopNameDTO) httpServletRequest.getSession().getAttribute("shopName");
        mainService.removeItem(shopNameDTO, id);
        return "redirect:/display";
    }

    @GetMapping("/modify_item")
    public String modifyItem(Model model, int id) {
        Optional<Item> optItem = mainService.getItem(id);
        if (optItem.isPresent()) {
            model.addAttribute("item", optItem.get());
            return "modify_item";
        }
        return "redirect:/display";
    }

    @PostMapping("/modify")
    public String modify(Item item) {
        ShopNameDTO shopNameDTO = (ShopNameDTO) httpServletRequest.getSession().getAttribute("shopName");
        mainService.update(shopNameDTO, item);
        return "redirect:/display";
    }
}
