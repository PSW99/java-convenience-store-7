package store.controller;

import store.domain.order.Orders;
import store.domain.product.NoPromotionProducts;
import store.domain.product.Product;
import store.domain.product.PromotionProducts;
import store.domain.purchasedProduct.PurchasedProduct;
import store.domain.receipt.Receipt;
import store.service.FileService;
import store.service.PaymentService;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class ConvenienceStoreController {
    private final ProductService productService;
    private final PaymentService paymentService;

    public ConvenienceStoreController() {
        this.productService = new ProductService(FileService.loadProducts());
        this.paymentService = new PaymentService();
    }

    public void startShopping() {
        OutputView.printWelcomeMessage();
        List<Product> products = productService.getProducts();
        OutputView.printProducts(products);

        PromotionProducts promotionProducts = productService.getPromotionProducts();
        NoPromotionProducts noPromotionProducts = productService.getNoPromotionProducts();

        List<PurchasedProduct> purchasedProducts = getPurchasedProducts(promotionProducts, noPromotionProducts);

        boolean hasMembershipDiscount = InputView.askForMembershipDiscount();
        Receipt receipt = new Receipt(purchasedProducts, hasMembershipDiscount);
        OutputView.printReceipt(receipt);

        if (InputView.confirmAdditionalPurchase()) {
            startShopping();
        }
    }

    private List<PurchasedProduct> getPurchasedProducts(PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        List<PurchasedProduct> purchasedProducts;
        while (true) {
            try {
                Orders orders = InputView.readOrders();
                purchasedProducts = paymentService.payment(orders, promotionProducts, noPromotionProducts);
                break;
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
        return purchasedProducts;
    }
}
