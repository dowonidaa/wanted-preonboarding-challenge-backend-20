package com.market.wanted.order.repository;

import com.market.wanted.member.entity.QMember;
import com.market.wanted.order.dto.QResponseOrder;
import com.market.wanted.order.dto.ResponseOrder;
import com.market.wanted.order.dto.QTransactionDetail;
import com.market.wanted.order.dto.TransactionDetail;
import com.market.wanted.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.market.wanted.order.entity.QOrder.order;
import static com.market.wanted.order.entity.QOrderItem.orderItem;
import static com.market.wanted.product.entity.QProduct.product;

@Repository
public class OrderFindRepositoryImpl implements OrderFindRepository{

    private final JPAQueryFactory queryFactory;

    public OrderFindRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ResponseOrder> findAllBySellerName(String username) {
        QMember seller = new QMember("seller");
        return queryFactory
                .select(new QResponseOrder(order.id.as("orderId"),
                        order.seller.id.as("sellerId"),
                        order.buyer.id.as("buyerId"),
                        product.id.as("productId"),
                        orderItem.price,
                        product.productName,
                        order.orderStatus
                        ))
                .from(order)
                .leftJoin(order.orderItem, orderItem)
                .leftJoin(orderItem.product, product)
                .leftJoin(product.seller, seller)
                .where(order.seller.username.eq(username))
                .fetch();
    }

    @Override
    public List<ResponseOrder> findAllByBuyerName(String username) {
        QMember seller = new QMember("seller");
        return queryFactory
                .select(new QResponseOrder(order.id.as("orderId"),
                        order.seller.id.as("sellerId"),
                        order.buyer.id.as("buyerId"),
                        product.id.as("productId"),
                        orderItem.price,
                        product.productName,
                        order.orderStatus
                ))
                .from(order)
                .leftJoin(order.orderItem, orderItem)
                .leftJoin(orderItem.product, product)
                .leftJoin(product.seller, seller)
                .where(order.buyer.username.eq(username))
                .fetch();
    }

    @Override
    public List<TransactionDetail> findOrdersBySellerName(Long productId, String sellerName) {
        QMember seller = new QMember("seller");
        QMember buyer = new QMember("buyer");
        return queryFactory.select(
                new QTransactionDetail(order.id.as("orderId"),
                        order.buyer.username.as("username"),
                        order.createDate.as("transactionDate"),
                        product.productName.as("productName")))
                .from(order)
                .join(order.seller, seller)
                .join(order.buyer, buyer)
                .join(order.orderItem, orderItem)
                .join(orderItem.product, product)
                .where(seller.username.eq(sellerName).and(buyer.username.eq(
                        queryFactory.select(buyer.username)
                                .from(order)
                                .join(order.orderItem.product, product)
                                .join(order.buyer, buyer)
                                .where(product.id.eq(productId))
                                .fetchOne()
                )))
                .fetch();
    }

    @Override
    public List<TransactionDetail> findOrdersByBuyerName(Long productId, String buyerName) {
        QMember seller = new QMember("seller");
        QMember buyer = new QMember("buyer");
        return queryFactory.select(
                        new QTransactionDetail(order.id.as("orderId"),
                                order.buyer.username.as("username"),
                                order.createDate.as("transactionDate"),
                                product.productName.as("productName")))
                .from(order)
                .join(order.seller, seller)
                .join(order.buyer, buyer)
                .join(order.orderItem, orderItem)
                .join(orderItem.product, product)
                .where(buyer.username.eq(buyerName).and(seller.username.eq(
                        queryFactory.select(seller.username)
                                .from(product)
                                .join(product.seller, seller)
                                .where(product.id.eq(productId))
                                .fetchOne()
                )))
                .fetch();
    }
}
