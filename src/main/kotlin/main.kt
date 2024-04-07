//just some examples on how this will be used. use the object any way se want. In good intentions
fun main() {
    val testCase = mangaDexScrap()
    //testCase.searchedMangas("tokyo")
    var mangaT = mangaObj()
    mangaT.mangaName = "Gareki Shoujo"
    mangaT.mangaUrl = "https://mangadex.tv/manga/manga-qh993790"
    mangaT.mangaImg = "https://avt.mkklcdnv6temp.com/11/y/28-1675086858.jpg"
    testCase.pulledMangaDetails(mangaT)
}