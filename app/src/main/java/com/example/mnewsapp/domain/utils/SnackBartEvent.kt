

sealed interface SnackBartEvent {
    data class ShowToast(val message: String) : SnackBartEvent
}