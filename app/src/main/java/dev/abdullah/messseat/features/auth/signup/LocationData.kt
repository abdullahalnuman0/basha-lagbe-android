package dev.abdullah.messseat.features.auth.signup

object LocationData {

    val districtByDivision = mapOf(
        "Dhaka" to listOf(
            "Dhaka", "Gazipur", "Narayanganj", "Tangail", "Manikganj",
            "Munshiganj", "Narsingdi", "Faridpur", "Gopalganj",
            "Madaripur", "Rajbari", "Shariatpur", "Kishoreganj"
        ),
        "Chattogram" to listOf(
            "Chattogram", "Cox's Bazar", "Cumilla", "Feni", "Brahmanbaria",
            "Noakhali", "Chandpur", "Lakshmipur", "Rangamati",
            "Khagrachhari", "Bandarban"
        ),
        "Rajshahi" to listOf(
            "Rajshahi", "Bogura", "Pabna", "Sirajganj", "Naogaon",
            "Natore", "Chapainawabganj", "Joypurhat"
        ),
        "Khulna" to listOf(
            "Khulna", "Jashore", "Kushtia", "Satkhira", "Bagerhat",
            "Jhenaidah", "Chuadanga", "Magura", "Meherpur", "Narail"
        ),
        "Barishal" to listOf(
            "Barishal", "Patuakhali", "Bhola", "Pirojpur", "Barguna", "Jhalokati"
        ),
        "Sylhet" to listOf(
            "Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"
        ),
        "Rangpur" to listOf(
            "Rangpur", "Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat",
            "Nilphamari", "Panchagarh", "Thakurgaon"
        ),
        "Mymensingh" to listOf(
            "Mymensingh", "Jamalpur", "Netrokona", "Sherpur"
        )
    )

    val divisions
        get() = districtByDivision.keys.toList()

    val popularAreasByDistrict = mapOf(
        "Dhaka" to listOf(
            "Dhanmondi", "Mirpur 1", "Mirpur 2", "Mirpur 10", "Mirpur 11", "Mirpur 12",
            "Uttara Sector 1-7", "Uttara Sector 8-14", "Uttara Sector 15-18",
            "Mohakhali", "Banani", "Gulshan 1", "Gulshan 2", "Badda",
            "Bashundhara R/A", "Mohammadpur", "Lalmatia", "Farmgate",
            "Panthapath", "Elephant Road", "Azimpur", "Khilgaon", "Malibagh",
            "Moghbazar", "Shantinagar", "Motijheel", "Nikunja 1 & 2",
            "Baridhara", "Baridhara DOHS", "Mirpur DOHS", "Mohakhali DOHS",
            "Uttara DOHS", "Shyamoli", "Kalyanpur", "Tejgaon", "Banasree",
            "Aftabnagar", "Rampura", "Jatrabari", "Puran Dhaka"
        ),
        "Gazipur" to listOf(
            "Board Bazar", "Chowrasta", "Tongi", "Joydebpur", "DMC Gate", "Salna"
        ),
        "Narayanganj" to listOf(
            "Chashara", "Signboard", "Kanchpur", "Siddhirganj", "Fatullah"
        ),
        "Chattogram" to listOf(
            "Agrabad", "GEC Circle", "Nasirabad", "Panchlaish", "Halishahar",
            "Khulshi", "Chawkbazar", "Muradpur", "Bahaddarhat", "Kotwali", "Anderkilla"
        ),
        "Sylhet" to listOf(
            "Zindabazar", "Amberkhana", "Shibganj", "Shahjalal Upashahar",
            "Subidbazar", "Tilagarh", "Chouhatta", "Akhalia"
        ),
        "Rajshahi" to listOf(
            "Kazihata", "Shaheb Bazar", "Motihar", "Padma Residential",
            "Upashahar", "Talaimari", "Laxmipur"
        ),
        "Khulna" to listOf(
            "Sonadanga", "Boyra", "Khalishpur", "Daulatpur", "Shibbari", "Mujgunni"
        ),
        "Barishal" to listOf(
            "Sadar Road", "Rupatali", "Nathullabad", "Nobogram Road", "Band Road"
        ),
        "Rangpur" to listOf(
            "Jahaj Company Mor", "Dhap", "Lalbagh", "Kachari Bazar", "Modern Mor"
        ),
        "Mymensingh" to listOf(
            "Ganginar Par", "Town Hall", "Chorpara", "Akua", "Kashor", "Maskanda"
        )
    )


    val popularInstitutions = listOf(
        "University of Dhaka (DU)",
        "Bangladesh University of Engineering and Technology (BUET)",
        "Dhaka Medical College (DMC)",
        "North South University (NSU)",
        "BRAC University",
        "Jahangirnagar University (JU)",
        "American International University-Bangladesh (AIUB)",
        "Ahsanullah University of Science and Technology (AUST)",
        "East West University (EWU)",
        "Independent University, Bangladesh (IUB)",
        "United International University (UIU)",
        "Islamic University of Technology (IUT)",
        "Shahjalal University of Science and Technology (SUST)",
        "Chittagong University (CU)",
        "Chittagong University of Engineering and Technology (CUET)",
        "Rajshahi University (RU)",
        "Rajshahi University of Engineering and Technology (RUET)",
        "Khulna University (KU)",
        "Khulna University of Engineering and Technology (KUET)",
        "Dhaka University of Engineering & Technology (DUET)",
        "Bangladesh Agricultural University (BAU)",
        "Sir Salimullah Medical College (SSMC)",
        "Mymensingh Medical College (MMC)",
        "Notre Dame College",
        "Dhaka College",
        "Working Professional / Corporate",
        "Govt. Service Holder",
        "Self-Employed / Freelancer"
    )
}