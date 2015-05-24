package component

import demo.model.Book
import org.grooscript.asts.GsNative
import org.grooscript.asts.RequireJsModule
import org.grooscript.jquery.GQuery
import org.grooscript.jquery.GQueryImpl
import org.grooscript.templates.Templates

class BookPresenter implements Chart {

    List<Book> books = []
    String urlBooks
    String booksListSelector
    String counterSelector
    Counter booksCounter
    GQuery gQuery = new GQueryImpl()
    boolean sortByTitle = false

    //New book properties
    String title
    String author
    String year

    @RequireJsModule(path = 'messages')
    def messages

    void init() {
        startCounter(counterSelector)
        bindNewBook()
        clearNewBook()
        gQuery('#clearBookButton').click(this.&clearNewBook)
        getBooksFromServer()
        println messages.salute
    }

    void addBookToServer() {
        Book book = new Book(author: author, title: title, year: year ? Integer.parseInt(year) : 0 )
        if (book.validate()) {
            gQuery.doRemoteCall('/addBook', 'POST', book, { data ->
                if (data.result == 'OK') {
                    clearNewBook()
                } else {
                    errorMessage 'Error', 'Validation server error adding book.'
                }
            }, { error ->
                errorMessage 'Error', "Server error adding book: ${error}"
            })
        } else {
            errorMessage('Nope', book.errorMessage())
        }
    }

    @GsNative
    void errorMessage(String head, String message) {/*
        swal(head, message, "error");
    */}

    private void startCounter(String counterSelector) {
        booksCounter = new Counter()
        booksCounter.onClickShow = this.&showListBooks
        booksCounter.start(counterSelector)
    }

    void newBookFromServer(Book book) {
        books << book
        if ($('.tableSearch')) {
            changeSearchText(gQuery('#marking').val())
        }
        updateBooksNumber(books.size())
        updateLastBook(book)
        drawPie()
    }

    void showListBooks() {
        if (books) {
            def data = [listBooks: books, searchString: '']
            gQuery(booksListSelector).html Templates.applyTemplate('bookList.gtpl', data)
            gQuery.onChange('marking', this.&changeSearchText)
            gQuery.onEvent('#hideListBooks', 'click', this.&hideListBooks)
            sortByTitleEvent()
        }
    }

    void changeSearchText(searchText) {
        def data = [listBooks: books, searchString: searchText, sortByTitle: sortByTitle]
        gQuery('.tableSearch').html Templates.applyTemplate('bookTable.gtpl', data)
        sortByTitleEvent()
    }

    void changeSort() {
        sortByTitle = !sortByTitle
        changeSearchText(gQuery('#marking').val())
    }

    void hideListBooks() {
        gQuery(booksListSelector).html ''
    }

    void clearNewBook() {
        setAuthor('')
        setTitle('')
        setYear('')
    }

    private bindNewBook() {
        gQuery.bindAllProperties(this)
        gQuery.onEvent('#addNewBook', 'click', this.&addBookToServer)
    }

    private getBooksFromServer() {
        gQuery.doRemoteCall(urlBooks, 'GET', null, { listBooks ->
            books = listBooks
            updateBooksNumber(listBooks.size())
            drawPie()
        }, { msg ->
            errorMessage 'Error', "Error getBooksFromServer: $msg"
        })
    }

    private updateBooksNumber(number) {
        booksCounter.number = number
    }

    private drawPie() {
        def groups = books.sort(false) { it.year }.
                           groupBy { it.year }
        def data = [
            labels: groups.collect { it.key }.reverse(),
            series: groups.collect { it.value.size() }.reverse()
        ]
        pieChart('.ct-chart', data)
    }

    private updateLastBook(Book book) {
        gQuery('#lastBook').html Templates.applyTemplate('lastBook.gtpl', [last: book])
    }

    private sortByTitleEvent() {
        gQuery.onEvent('#titleHead', 'click', this.&changeSort)
    }
}
