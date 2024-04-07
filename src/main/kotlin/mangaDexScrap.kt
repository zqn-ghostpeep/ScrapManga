import org.jsoup.Jsoup
import java.io.File
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
        try {
            //base variables
            var webGood = false

            //checks to make sure the url is all good
            val url = URL(manga.mangaUrl)
            val urlc = url.openConnection() as HttpURLConnection
            urlc.connectTimeout = 10 * 1000 // 10 s.
            urlc.connect()
            webGood = urlc.responseCode == 200

            //end process if url is not up or good
            if (!webGood) {
                println("WEB SITE IS DOWN")//remove
                return
            }
            println("We are all good chief")//remove

            //pulling html and main parts to pull the manga details
            val mainHtmlDoc = Jsoup.connect(manga.mangaUrl).get()
            val mostMangaDetailsDoc = mainHtmlDoc.getElementsByClass("manga-info-text")
            val mangaSummaryDoc = mainHtmlDoc.getElementById("noidungm")
            val mangaChapterListDoc = mainHtmlDoc.getElementsByClass("chapter-list")

            //get the summary of the manga
            manga.mangaSummary = mangaSummaryDoc?.text().toString().trim()

            //get the rest of the detail items
            val listDetailItems = mostMangaDetailsDoc[0].select("li")
            for (listItem in listDetailItems){
                //author
                if(listItem.text().contains("author",ignoreCase = true)){
                    manga.mangaAuthor = listItem.text().substringAfter(":").trim()
                }
                //status
                else if(listItem.text().contains("status",ignoreCase = true)){
                    manga.mangaStatus = listItem.text().substringAfter(":").trim()
                }
                //genres
                else if (listItem.text().contains("genres",ignoreCase = true)){
                    val listOfGeres = listItem.text().substringAfter(":").split(",")
                    manga.mangaGenresList = listOfGeres as ArrayList<String>
                    manga.mangaGenresList.removeLast()
                }
            }

            //pulls the chapters with the chapter url and the name
            val listChapterItems = mangaChapterListDoc[0].children().select("a")
            for (eachRow in listChapterItems){
                val mangaC = mangaChapter()
                mangaC.mangaChapterName = eachRow.text()
                mangaC.mangaChapterURl = baseSearchURL + eachRow.attr("href").toString()
                manga.mangaChapterList.add(mangaC)
            }
            println(manga.mangaName)
            println(manga.mangaAuthor)
            println(manga.mangaImg)
            println(manga.mangaStatus)
            println(manga.mangaSummary)
            println(manga.mangaUrl)
            println("Number of chapters: "+manga.mangaChapterList.size.toString())
            println("Genres")
            for(g in manga.mangaGenresList){
                println(g.trim())
            }
            for(c in manga.mangaChapterList){
                println(c.mangaChapterName)
                println(c.mangaChapterURl)
            }
        }
        //if error display message to user
        catch (e: Exception){
            println("THERE HAS BEEN A ERROR")
            println(e.message)
            val last = e.stackTrace.lastIndex
            println(e.stackTrace[last-2].methodName)
            println(e.stackTrace[last-2].lineNumber)
        }
    }

    //pulls the images of the selected chapter
    fun getChapterImages(chpaterUrl: String) {

    }
}