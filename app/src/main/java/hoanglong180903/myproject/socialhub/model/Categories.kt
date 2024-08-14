package hoanglong180903.myproject.socialhub.model

data class Categories (
    var id : Int,
    var phone : List<Product>,
    var name_brand: String,
    var image_brand:String,
)

data class Product(
    var id : Int,
    var name :String,
    var image:String,
    var price:Int,
    var rating:Float
)