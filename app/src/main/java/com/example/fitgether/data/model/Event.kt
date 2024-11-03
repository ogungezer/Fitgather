package com.example.fitgether.data.model

data class Event(
    val eventCreatorId : String = "",
    val joinedUserIds : MutableList<String> = mutableListOf(),
    val eventCommentIds : MutableList<String> = mutableListOf(),
    val eventCategory : String = "",
    val eventTitle : String = "",
    val eventDescription : String = "",
    val eventLocation : List<Double> = emptyList(),
    val eventAddress : String = "",
    val eventDate : String = "",
    val eventTime : String = "",
    val genderChoice : String = "",
    val maxPerson : Int = 99,
    val eventRating : Float? = null,
    val isEventEnable : Boolean = true,
    )







//deneme için bunu sil.
val eventList = listOf(
    Event(
        eventTitle = "Bisiklete binme etkinliği kartal sahil sınırlı kişi",
        eventDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
        joinedUserIds = mutableListOf("sdfsdfsdf", "sdfsdfq", "fwerewrwr"),
        maxPerson = 55,
        eventDate = "22/07/2024",
        eventTime = "14:20",
        eventAddress = "Kordonboyu, Çetin Emeç Blv, 34860 Kartal/İstanbul"
    ),
    Event(
        eventTitle = "Yürüyüş ve Doğa Yürüyüşü",
        eventDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
        joinedUserIds = mutableListOf("user1", "user2", "user3", "user4"),
        maxPerson = 75,
        eventDate = "12/08/2024",
        eventTime = "08:00",
        eventAddress = "Aydos Ormanı, Pendik/İstanbul"
    ),
    Event(
        eventTitle = "Deniz Kenarında Yoga Etkinliği",
        eventDescription = "Join us for a refreshing yoga session by the sea. Perfect for beginners and experts alike.",
        joinedUserIds = mutableListOf("userA", "userB"),
        maxPerson = 40,
        eventDate = "29/06/2024",
        eventTime = "06:30",
        eventAddress = "Caddebostan Sahili, Kadıköy/İstanbul"
    ),
    Event(
        eventTitle = "Açık Hava Sinema Gecesi",
        eventDescription = "Watch a classic movie under the stars. Bring your own snacks and drinks.",
        joinedUserIds = mutableListOf("movieLover1", "movieLover2", "movieLover3", "movieLover4", "movieLover5"),
        maxPerson = 99,
        eventDate = "15/09/2024",
        eventTime = "20:00",
        eventAddress = "Moda Sahnesi, Kadıköy/İstanbul"
    ),
    Event(
        eventTitle = "Sahil Temizliği Gönüllü Etkinliği",
        eventDescription = "Help us keep our beaches clean. Gloves and bags will be provided.",
        joinedUserIds = mutableListOf("volunteer1", "volunteer2", "volunteer3", "volunteer4", "volunteer5", "volunteer6"),
        maxPerson = 50,
        eventDate = "05/10/2024",
        eventTime = "10:00",
        eventAddress = "Florya Plajı, Bakırköy/İstanbul"
    ),
    Event(
        eventTitle = "Kite Flying Festival",
        eventDescription = "Bring your own kite or buy one at the event. Fun for all ages.",
        joinedUserIds = mutableListOf("kiteLover1", "kiteLover2"),
        maxPerson = 80,
        eventDate = "18/07/2024",
        eventTime = "15:00",
        eventAddress = "Sarıyer Sahili, Sarıyer/İstanbul"
    ),
    Event(
        eventTitle = "Bahar Pikniği ve Oyunlar",
        eventDescription = "Enjoy a lovely picnic with games and activities for everyone.",
        joinedUserIds = mutableListOf("picnicFan1", "picnicFan2", "picnicFan3"),
        maxPerson = 60,
        eventDate = "25/05/2025",
        eventTime = "12:00",
        eventAddress = "Belgrad Ormanı, Sarıyer/İstanbul"
    ),
    Event(
        eventTitle = "Kamp ve Doğa Yürüyüşü",
        eventDescription = "Spend a night under the stars and hike through beautiful trails.",
        joinedUserIds = mutableListOf("camper1", "camper2", "camper3", "camper4", "camper5", "camper6", "camper7"),
        maxPerson = 30,
        eventDate = "10/11/2024",
        eventTime = "16:00",
        eventAddress = "Polonezköy Tabiat Parkı, Beykoz/İstanbul"
    ),
    Event(
        eventTitle = "Sokak Sanatları Festivali",
        eventDescription = "Experience the vibrant street art scene with performances and installations.",
        joinedUserIds = mutableListOf("artist1", "artist2", "artist3", "artist4", "artist5", "artist6", "artist7", "artist8"),
        maxPerson = 90,
        eventDate = "29/06/2025",
        eventTime = "18:00",
        eventAddress = "Galata Kulesi, Beyoğlu/İstanbul"
    )
)
//sillll
