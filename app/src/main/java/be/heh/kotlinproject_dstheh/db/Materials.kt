package be.heh.kotlinproject_dstheh.db

class Materials(i:Int) {
    var id:Int=0
        private set
    var type : String="null"
        private set
    var modele : String="null"
        private set
    var website:String="null"
        private set
    var quantity:Int=0
    constructor(i: Int,t:String,m:String,w:String,q:Int):this(i)
    {
        id=i
        type=t
        modele=m
        website=w
        quantity=q
    }
    override fun toString(): String {
        val sb=StringBuilder()
        sb.append("ID : "+id.toString()+"\n"+
                "Type : "+ type +"\n"+
                "Modèle : "+modele +"\n"+
                "Website : "+website +"\n"+
                "Quantity : "+quantity.toString()
        )
        return sb.toString()
    }
}