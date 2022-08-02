class App {
    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                position: 'top-end',
                text: t,
                timer: 1500
            })
        }

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
    constructor(id, username, password, fullName, phone, urlImage, locationRegion, role) {
        this.id = id;
        this.username= username;
        this.password = password
        this.fullName = fullName;
        this.phone = phone;
        this.urlImage = urlImage;
        this.locationRegion = locationRegion;
        this.role = role;
    }
}
class Role {
    constructor(id, code) {
        this.id = id;
        this.code = code;
    }
}

class Product {
    constructor(id, price, quantity, title, stopSelling, urlImage, category) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.stopSelling = stopSelling;
        this.urlImage = urlImage;
        this.category = category;
    }
}

class Category{
    constructor(id , title) {
        this.id = id;
        this.title = title;
    }
}
