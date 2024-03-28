//Base of the manga
class mangaObj {
    var mangaUrl: String = "NO URL"
    var mangaImg: String = "NO IMAGE"
    var mangaName: String = "NO NAME"
    var mangaSummary: String = "NO SUMMARY"
    var mangaAuthor: String = "NO AUTHOR"
    var mangaStatus: String = "NO STATUS"
    //in a list since these are planned to be used as hyperlinks
    var mangaGenresList: ArrayList<String> = ArrayList<String>()
    var mangaChapterList: ArrayList<mangaChapter> = ArrayList<mangaChapter>()
}
//base of a chapter
class mangaChapter {
    var mangaChapterName: String = "NO CHAPTER"
    var mangaChapterURl: String = "NO URL"
    }