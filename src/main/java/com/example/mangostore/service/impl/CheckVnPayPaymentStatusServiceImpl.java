package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.service.CheckVnPayPaymentStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CheckVnPayPaymentStatusServiceImpl implements CheckVnPayPaymentStatusService {
    private final Gender gender;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final AccountRepository accountRepository;
    private final RankRepository rankRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;

    public CheckVnPayPaymentStatusServiceImpl(Gender gender,
                                              InvoiceRepository invoiceRepository,
                                              InvoiceDetailRepository invoiceDetailRepository,
                                              ProductDetailRepository productDetailRepository,
                                              AccountRepository accountRepository,
                                              RankRepository rankRepository,
                                              VoucherClientRepository voucherClientRepository,
                                              ShoppingCartRepository shoppingCartRepository,
                                              ShoppingCartDetailRepository shoppingCartDetailRepository) {
        this.gender = gender;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.accountRepository = accountRepository;
        this.rankRepository = rankRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
    }

    @Override
    public String bankingSuccess(HttpServletRequest request, HttpSession session) {
        Long idInvoice = (Long) session.getAttribute("idInvoice");
        if (idInvoice != null) {
            int paymentStatus = gender.orderReturn(request);
            if (paymentStatus == 1) {
                Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
                assert invoice != null;
                invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                invoice.setReturnClientMoney(invoice.getTotalPayment());
                invoice.setPayments("banking");
                invoice.setLeftoverMoney(0);
                invoice.setInvoiceStatus(6);
                invoiceRepository.save(invoice);

                List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.getAllInvoiceDetailByIdInvoice(idInvoice);
                for (InvoiceDetail detail : getAllInvoiceDetail) {
                    ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
                    assert productDetail != null;
                    Integer quantityNew = productDetail.getQuantity() - detail.getQuantity();
                    productDetail.setQuantity(quantityNew);
                    productDetailRepository.save(productDetail);
                }

                if (invoice.getIdCustomer() != null) {
                    Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
                    Integer addPoints = gender.roundingNumber(rewardPoints);
                    Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                    assert detailAccount != null;
                    Integer points = detailAccount.getAccumulatedPoints() + addPoints;
                    detailAccount.setAccumulatedPoints(points);
                    accountRepository.save(detailAccount);

                    List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
                    itemsRank.sort((rank1, rank2) -> rank2.getMaximumScore().compareTo(rank1.getMaximumScore()));
                    for (Rank rank : itemsRank) {
                        if (detailAccount.getAccumulatedPoints() >= rank.getMinimumScore() && detailAccount.getAccumulatedPoints() < rank.getMaximumScore()) {
                            detailAccount.setRank(rank);
                            break;
                        } else if (detailAccount.getAccumulatedPoints() >= rank.getMaximumScore()) {
                            detailAccount.setRank(itemsRank.get(0));
                            break;
                        }
                    }
                    accountRepository.save(detailAccount);
                    session.removeAttribute("idInvoice");
                }
                return "redirect:/mangostore/admin/sell";
            } else {
                return "redirect:/mangostore/admin/sell/edit?id=" + idInvoice;
            }
        }

        Long idInvoiceClient = (Long) session.getAttribute("idInvoiceClient");
        if (idInvoiceClient != null) {
            int paymentStatus = gender.orderReturn(request);
            if (paymentStatus == 1) {
                Invoice invoice = invoiceRepository.findById(idInvoiceClient).orElse(null);
                assert invoice != null;
                invoice.setInvoiceForm("online");
                invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                invoice.setPayments("banking");
                invoice.setInvoiceStatus(1);
                invoiceRepository.save(invoice);

                List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoice(idInvoiceClient);
                for (InvoiceDetail detail : getAllInvoiceDetail) {
                    ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
                    assert productDetail != null;
                    Integer quantityNew = productDetail.getQuantity() - detail.getQuantity();
                    productDetail.setQuantity(quantityNew);
                    productDetailRepository.save(productDetail);
                }

                Account detailAccountToShoppingCart = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                assert detailAccountToShoppingCart != null;
                ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccountToShoppingCart.getId());
                detailShoppingCart.setTotalShoppingCart(0);
                shoppingCartRepository.save(detailShoppingCart);

                List<ShoppingCartDetail> findAllShoppingCart = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
                for (ShoppingCartDetail shoppingCartDetail : findAllShoppingCart) {
                    shoppingCartDetail.setStatus(0);
                    shoppingCartDetailRepository.save(shoppingCartDetail);
                }

                if (invoice.getVoucherClient() != null) {
                    VoucherClient voucherClient = voucherClientRepository.findById(invoice.getVoucherClient().getId()).orElse(null);
                    assert voucherClient != null;
                    voucherClient.setStatus(0);
                    voucherClientRepository.save(voucherClient);
                }

                if (invoice.getCustomerPoints() != 0) {
                    Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                    assert detailAccount != null;
                    detailAccount.setAccumulatedPoints(0);
                    accountRepository.save(detailAccount);
                }

                if (invoice.getIdCustomer() != null) {
                    Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
                    Integer addPoints = gender.roundingNumber(rewardPoints);
                    Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                    assert detailAccount != null;
                    Integer points = detailAccount.getAccumulatedPoints() + addPoints;
                    detailAccount.setAccumulatedPoints(points);
                    accountRepository.save(detailAccount);

                    List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
                    itemsRank.sort((rank1, rank2) -> rank2.getMaximumScore().compareTo(rank1.getMaximumScore()));
                    for (Rank rank : itemsRank) {
                        if (detailAccount.getAccumulatedPoints() >= rank.getMinimumScore() && detailAccount.getAccumulatedPoints() < rank.getMaximumScore()) {
                            detailAccount.setRank(rank);
                            break;
                        } else if (detailAccount.getAccumulatedPoints() >= rank.getMaximumScore()) {
                            detailAccount.setRank(itemsRank.get(0));
                            break;
                        }
                    }
                    accountRepository.save(detailAccount);
                    session.removeAttribute("idInvoiceClient");
                }
                return "redirect:/mangostore/home";
            } else {
                return "redirect:/mangostore/cart/checkout";
            }
        }
        return "";
    }
}
