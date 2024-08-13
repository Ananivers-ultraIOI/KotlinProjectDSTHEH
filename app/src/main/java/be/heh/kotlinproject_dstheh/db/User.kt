package be.heh.kotlinproject_dstheh.db

class User(i : Int) {
    private var id:Int=0
    var email : String="null"
        private set
    var pwd : String="null"
        private set
    var rights:Int=0
        private set
    constructor(i: Int,e:String,p:String,r:Int):this(i)
    {
        id=i
        email=e
        pwd=p
        rights=r
    }

    override fun toString(): String {
        val sb=StringBuilder()
        sb.append("ID : "+id.toString()+"\n"+
            "Email : "+ email +"\n"+
            "Password : "+pwd +"\n"+
            "Right : "+rights.toString()
        )
        return sb.toString()
    }
}