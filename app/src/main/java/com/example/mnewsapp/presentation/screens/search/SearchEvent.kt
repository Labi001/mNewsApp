
sealed class SearchEvent {

    data class OnQueryChange(val query: String,val from: String,val to: String) : SearchEvent()
    object OnSearch : SearchEvent()

}
