package com.example.scandia.model


data class Article(
    val url: String,
    val imgUrl: String,
    val title: String
)

fun generateDummyArticle() = listOf(
    Article(
        url = "https://www.chop.edu/conditions-diseases/hypospadias",
        imgUrl = "https://www.chop.edu/sites/default/files/styles/16_9_large/public/curvature-775x257.jpg?itok=Pzo5fdsK",
        title = "What is hypospadias?"
    ),
    Article(
        url = "https://www.nature.com/articles/s41585-021-00555-0",
        imgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9k4fBBwumR0ZE0qk4jyQSUORSl3xD5XAPWg&usqp=CAU",
        title = "Surgical management of primary severe hypospadias in children: an update focusing on penile curvature"
    ),
    Article(
        url = "https://doi.org/10.1016/S0022-5347(01)68107-2",
        imgUrl = "https://d3i71xaburhd42.cloudfront.net/291ee9be1e76259fb4030daa9bf36c5e1420a722/1-Figure1-1.png",
        title = "CHORDEE CORRECTION BY CORPORAL ROTATION: THE SPLIT AND ROLL TECHNIQUE"
    ),
    Article(
        url = "https://link.springer.com/chapter/10.1007/978-3-642-20641-2_126",
        imgUrl = "https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-642-20641-2_126/MediaObjects/978-3-642-20641-2_126_Fig1_HTML.gif",
        title = "F13 Principles of Hypospadias Surgery"
    ),
    Article(
        url = "https://www.sciencedirect.com/science/article/abs/pii/S1477513122001541",
        imgUrl = "https://ars.els-cdn.com/content/image/1-s2.0-S1477513122001541-fx1.jpg",
        title = "How accurate is eyeball measurement of curvature? A tool for hypospadias surgery"
    ),
    Article(
        url = "https://link.springer.com/chapter/10.1007/978-981-16-8395-4_6",
        imgUrl = "https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-981-16-8395-4_6/MediaObjects/495391_1_En_6_Fig16_HTML.png",
        title = "Chordee Correction in Hypospadias Repair"
    ),
    Article(
        url = "https://doi.org/10.7759%2Fcureus.27544",
        imgUrl = "https://assets.cureus.com/uploads/figure/file/423965/article_river_bf1bce4010bc11edacc725039994fc76-Hypospadias-classification.png",
        title = "Hypospadias: A Comprehensive Review Including Its Embryology, Etiology and Surgical Techniques"
    ),
    Article(
        url = "https://link.springer.com/chapter/10.1007/978-3-030-94248-9_8",
        imgUrl = "https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-030-94248-9_8/MediaObjects/510035_2_En_8_Fig36_HTML.png",
        title = "The Urethral Plate and Chordee"
    ),
    Article(
        url = "https://www.cincinnatichildrens.org/health/h/hypospadias-chordee",
        imgUrl = "https://www.cincinnatichildrens.org/health/h/-/media/adafef87bd224ed381d01ae4cb970906.ashx",
        title = "What is Hypospadias and Chordee?"
    ),
    Article(
        url = "https://link.springer.com/article/10.1007/BF02549647",
        imgUrl = "https://media.springernature.com/w158/springer-static/cover-hires/journal/11255?as=webp",
        title = "Definition and treatment of chordee without hypospadias: A report of 5 cases"
    ),
).shuffled()
