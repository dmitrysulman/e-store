let csrfToken;
let csrfHeader;
let headers = {};
$(document).ready(function () {
    let csrfToken = $("meta[name='_csrf']").attr("content");
    let csrfHeader = $("meta[name='_csrf_header']").attr("content");
    headers[csrfHeader] = csrfToken;
});

function removeProduct(el, productId, completely) {
    el.prop("disabled", true);
    let dataRemove = {};
    dataRemove.productId = productId;
    dataRemove.completely = completely;
    $.ajax({
        type: "POST",
        url: "/cart/remove_from_cart",
        headers: headers,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataRemove),
        success: function (data) {
            el.prop("disabled", false);
            location.reload();
        },
        error: function () {
            el.prop("disabled", false);
        }
    });
}

function addToCart(el, productId) {
    let data = {};
    data.productId = productId;
    el.prop("disabled", true);
    $.ajax({
        type: "POST",
        url: "/cart/add_to_cart",
        headers: headers,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(data),
        success: function () {
            el.prop("disabled", false);
            location.reload();
        },
        error: function () {
            el.prop("disabled", false);
        }
    });
}

function makeOrder(el, data) {
    el.prop("disabled", true);
    $.ajax({
        type: "POST",
        url: "/cart/order",
        headers: headers,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(data),
        success: function (data) {
            el.prop("disabled", false);
            alert("Ok! Order number: " + data.id);
            location.href = "/";
        },
        error: function () {
            el.prop("disabled", false);
            alert("Not ok");
        }
    });
}

function clearCart(el) {
    el.prop("disabled", true);
    $.ajax({
        type: "POST",
        url: "/cart/clear_cart",
        headers: headers,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            el.prop("disabled", false);
            location.reload();
        },
        error: function () {
            el.prop("disabled", false);
        }
    });
}