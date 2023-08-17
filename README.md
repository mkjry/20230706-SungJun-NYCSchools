# 20230706-SungJun-NYCSchools

Demonstrate of..

Kotlin, Coroutine
API data call with Retrofit
MVVM with Live Data, Observer pattern
View Bindine/ Data Binding with Live Data
Recyhclerview with Click event for 2nd API call

Logic flows..

Get the Schools list from API request
Kotlin Coroutine > ViewModelScope > Retrofit API call
> get Schools List > PostValue through LiveData on ViewModel Class
> Show on Recyclerview with Data Binding form Observed LiveData
> Click event on RecyclerView list for one School item
> 2nd API call via Retrofit API call
> Get SAT data details for clicked School
> Show on UI Screen
> Click event on SAT data detail screen
> Return to RecyclerView to return Schools list 
