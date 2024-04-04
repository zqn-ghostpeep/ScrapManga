import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

//that actual scrapping of the website
//on this first episode we are we'll be pulling from manga dex

class mangaDexScrap {
    //where all the searched stuff goes
    var theSearchedList: ArrayList<mangaObj> = ArrayList<mangaObj>()
    val baseSearchURL = "https://mangadex.tv"
    var totalPages = "1"
    //a list of manga to choose from when the search result is pulled
    fun searchedMangas(searchWord: String) {
        try {
            //checks to make sure the url is all good
            var webGood = false
            val url = URL("$baseSearchURL/search/$searchWord?page=1")
            val urlTest = url.openConnection() as HttpURLConnection
            urlTest.connectTimeout = 10 * 1000 // 10 s.
            urlTest.connect()
            webGood = urlTest.responseCode == 200
            //end process if url is not up or good
            if (!webGood) {
                println("WEB SITE IS DOWN")
                return
            }
            println("We are all good chief")

            //pulling html doc
            val mainHtmlDoc = Jsoup.connect(url.toString()).get()

            //this get the list of manga in the search page
            val searchList = mainHtmlDoc.getElementsByClass("panel_story_list")

            //this is the last page number of the manga
            val pageNumber = mainHtmlDoc.getElementsByClass("page_blue page_last")
            totalPages = pageNumber.text().substringAfter("(").substringBefore(")")

            //gets the main part of the searched manga
            val searchListString = searchList.toString()
            val splitSearchString = searchListString.split("\n").toTypedArray()
            val mangaSearchObject = ArrayList<mangaObj>()

            //creates manga object for each item in the main search body
            var manga: mangaObj
            //pulls the important parts to fill the manga object and adds them to a list
            if(splitSearchString.isNotEmpty()) {
                for (line in splitSearchString) {
                    if (line.contains("manga", ignoreCase = true) and line.contains("story_item", ignoreCase = true)) {
                        val docMangaURl = Jsoup.parse(line).body().getElementsByTag("a")
                        val imageElement = docMangaURl.select("img").first()
                        //gets manga name,url and image
                        manga = mangaObj()
                        manga.mangaUrl = baseSearchURL + docMangaURl.attr("href").toString()
                        manga.mangaImg = imageElement?.attr("src").toString()
                        manga.mangaName = imageElement?.attr("alt").toString()
                        mangaSearchObject.add(manga)
                    }
                }
            }
            for(m in mangaSearchObject){
                println(m.mangaName)
            }
        }
        //if error display message to user
        catch (e: Exception){
            println("THERE HAS BEEN A ERROR")
            println(e.message)
        }
    }
    //pulls the manga details once a single manga was selected
    fun pulledMangaDetails(manga: mangaObj){

    }

    //pulls the images of the selected chapter
    fun getChapterImages(chpaterUrl: String) {

    }
}