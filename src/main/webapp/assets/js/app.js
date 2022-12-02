
class App{
    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'top-end',
                showConfirmButton: false,
                timer: 2000
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                position: 'top-end',
                text: t,
                timer: 2000
            })
        }

        /*Hiển thị xóa*/
        static showConfirmDelete() {
            return Swal.fire({
                title: 'Are you sure to deactive the selected customer ?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085D6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, deactive it!',

            });
        }

        /*Hiển thị khôi phục(Restore) nên tắt icon*/
        static showConfirmRestore() {
            return Swal.fire({
                title: 'Are you sure to Restore?',
                icon: 'success',
                showCancelButton: true,
                confirmButtonColor: '#3085D6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, restore it!',
            });
        }
    }

    static IziToast = class  {
        static showErrorAlert(m) {
            iziToast.error({
                title: 'Error',
                position: 'topLeft',
                message: m,
            });
        }

        static showSuccessAlert(m) {
            iziToast.success({
                title: 'Success',
                position: 'topLeft',
                message: m,
            });
        }

    }
    static formatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "").replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }

}


class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}
class User {
    constructor(id, username, password, fullName, phone, urlImage, deleted = 0, locationRegion, role, createdAt,updatedAt) {
        this.id = id;
        this.username= username;
        this.password = password
        this.fullName = fullName;
        this.phone = phone;
        this.urlImage = urlImage;
        this.deleted = deleted
        this.locationRegion = locationRegion;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }
}
class Role {
    constructor(id, code) {
        this.id = id;
        this.code = code;
    }
}

class Product {
    constructor(id, urlImage, name, price, quantity, describe,deleted = 0, category, createdAt,updatedAt) {
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.describe = describe;
        this.deleted = deleted
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

class Category{
    constructor(id , title) {
        this.id = id;
        this.title = title;
    }
}

class Cart {
    constructor(userId,productId,quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
}

class CartInfo {
    constructor(id,grandTotal,user) {
        this.id = id;
        this.grandTotal = grandTotal;
        this.user = user;
    }
}

class CartItem {
    constructor(id, price, quantity, title, totalPrice, product, cart) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.totalPrice = totalPrice;
        this.product = product;
        this.cart = cart;
    }
}


class Order {
    constructor(userId) {
        this.userId = userId;
    }
}

class OrderItem {
    constructor(id, title, price, quantity,totalPrice, urlImage, order) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.urlImage = urlImage;
        this.order = order;
    }
}

class OrderTest { /*day la order*/
    constructor(id, grandTotal, createdAt, user, orderStatus) {
        this.id = id;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
        this.user = user;
        this.orderStatus = orderStatus;
    }
}

class OrderStatus{
    constructor(id , title) {
        this.id = id;
        this.title = title;
    }
}
