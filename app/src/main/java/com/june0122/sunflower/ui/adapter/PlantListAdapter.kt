package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.viewholder.PlantListViewHolder
import com.june0122.sunflower.utils.PlantSelectedListener

class PlantListAdapter(private val listener: PlantSelectedListener) : RecyclerView.Adapter<PlantListViewHolder>() {
    var items = arrayListOf(
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/tFrVqLO3iqBY5hbJiyXGGbyoYUU=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-alocasia-indoors-1902735-03-e84e0164715746a0aee5b1a83aefe4ca.jpg",
            name = "Alocasia",
            description = "Tropical plants in the Alocasia genus feature stunning foliage that can become the centerpiece of a garden or room. Large rhizomes or tubers produce enormous heart-shaped or arrow-shaped ears, leading to the popular common name, elephant's ear. Fast-growing Alocasias are most often grown as houseplants, but it's common to bring them outdoors during the warm months, sometimes burying the entire pot in the ground to create a natural look."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/6cmK-3Xaef5wq8F7T40V7PM4bpE=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/growing-and-caring-for-dahlias-1402255-03-306dc2f2b09849189998f1bbe86b2a44.jpg",
            name = "Dahlia",
            description = "Dahlias are late-season bloomers. They bloom from mid-summer through the first frost and are available in a vast array of colors, patterns, bloom sizes, and flower forms. Plant size ranges from compact border dahlias to plate-sized blossoms atop 6-foot plants. Native to Mexico and Central America, over 20,000 dahlia cultivars have become darlings of plant breeders and flower shows alike."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/TCvGeFy2wwcxwviyXlB8WkjnckY=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-great-garlic-3016629-01-cbd93c768adb47b68137ad642e5033b9.jpg",
            name = "Garlic",
            description = "A close relative of the onion, garlic is an edible, bulbous plant native to Asia that has been cultivated for several thousand years. Above ground, garlic appears as flattened, grass-like leaves (also known as scapes). In contrast, below ground, it forms a firm bulb, typically containing between four and 20 cloves, encased in a papery exterior.\n" +
                    "\n" +
                    "Garlic should be planted in the fall, about a month or so before the first frost. Harvesting garlic is not an exact science. It will grow slowly over the next nine months or so and will deliver a bountiful harvest by mid-spring or summer. Garlic is known to be toxic to animals."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/3CgcaOl865Utv7eOEgbPemJB-Ng=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-jackfruit-indoors-1902445-02-5c7391cd861e4a9a87ddb11eebecc922.jpg",
            name = "Jackfruit",
            description = "The tropical jackfruit tree (Artocarpus heterophyllus) is a large evergreen tree that bears edible fruit. It has a relatively fast growth rate, and new trees can start producing fruit within a few years. It’s best planted in the spring. The tree trunk grows fairly straight and has reddish-brown bark. From it extend large branches with glossy green leaves that are around 8 inches long. The tree produces showy green flowers mostly in the fall, though it can bloom sporadically at other points in the year. Its yellow-green, kidney bean-shaped fruits are notoriously huge, maturing on the tree in the midsummer. They average between 10 and 40 pounds, though some fruits have been known to weigh 80 pounds or more. The inner yellow flesh is mildly sweet and has been compared to bananas and pineapples."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/wFm7wd800OqfpvlK0GHdjHTfHVI=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/nemesia-plant-profile-5070320-11-f8453f1be78541a7930b4d61c19d2a3f.jpg",
            name = "Nemesia",
            description = "The Nemesia plant may resemble an orchid, but it’s actually a small bedding plant that has an array of implications for any garden. Often incorporated into landscaping as edging plants or ground covers—as well as in mixed borders, woodland plantings, rock gardens, and hanging basket plants—the Nemesia is a vibrant and maintenance flower.\n" +
                    "\n" +
                    "The two most popular species are N. strumosa and N. caerulea. Most varieties of Nemesia can grow to about a foot in height, but some can be as tall as two feet. The versatile plants also come in a wide range of colors, and some are even bi-colored. The N. strumosa variety is a true annual that produces one-inch blue or white flowers and grows up to a foot tall, while N. caerulea is a more tender perennial that grows up to about a half-inch and blooms in purple, pink, blue, and white on plants that grow up to two feet.\n" +
                    "\n" +
                    "At a distance, these plants may also look like tiny snapdragons or edging lobelia, with flowers that can completely cover low-growing foliage (the top four petals form a fan with one large and sometimes lobed petal underneath). As long as temperatures remain mild, the plant can produce so many flowers that they almost entirely obscure any other foliage."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/-W1mZNWR5tVwvp4reWuEoe0ZEyc=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/GettyImages-902133540-7e603f4ab7aa4aadb097c61d2627da24.jpg",
            name = "Quinoa",
            description = "Quinoa (Chenopodium quinoa) is a flowering plant in the Amaranth family that is grown as a crop primarily for its edible seeds. It has been grown for human consumption for thousands of years, originating from mountainous regions of South America. Quinoa cultivation has now grown to over 70 countries around the world. This ancient superfood is packed with vitamins and minerals and has a nice mild taste. The seeds are often cooked like rice, or ground into a flour that can be used as a gluten-free alternative in cooking and baking."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/tFrVqLO3iqBY5hbJiyXGGbyoYUU=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-alocasia-indoors-1902735-03-e84e0164715746a0aee5b1a83aefe4ca.jpg",
            name = "Alocasia",
            description = "Tropical plants in the Alocasia genus feature stunning foliage that can become the centerpiece of a garden or room. Large rhizomes or tubers produce enormous heart-shaped or arrow-shaped ears, leading to the popular common name, elephant's ear. Fast-growing Alocasias are most often grown as houseplants, but it's common to bring them outdoors during the warm months, sometimes burying the entire pot in the ground to create a natural look."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/6cmK-3Xaef5wq8F7T40V7PM4bpE=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/growing-and-caring-for-dahlias-1402255-03-306dc2f2b09849189998f1bbe86b2a44.jpg",
            name = "Dahlia",
            description = "Dahlias are late-season bloomers. They bloom from mid-summer through the first frost and are available in a vast array of colors, patterns, bloom sizes, and flower forms. Plant size ranges from compact border dahlias to plate-sized blossoms atop 6-foot plants. Native to Mexico and Central America, over 20,000 dahlia cultivars have become darlings of plant breeders and flower shows alike."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/TCvGeFy2wwcxwviyXlB8WkjnckY=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-great-garlic-3016629-01-cbd93c768adb47b68137ad642e5033b9.jpg",
            name = "Garlic",
            description = "A close relative of the onion, garlic is an edible, bulbous plant native to Asia that has been cultivated for several thousand years. Above ground, garlic appears as flattened, grass-like leaves (also known as scapes). In contrast, below ground, it forms a firm bulb, typically containing between four and 20 cloves, encased in a papery exterior.\n" +
                    "\n" +
                    "Garlic should be planted in the fall, about a month or so before the first frost. Harvesting garlic is not an exact science. It will grow slowly over the next nine months or so and will deliver a bountiful harvest by mid-spring or summer. Garlic is known to be toxic to animals."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/3CgcaOl865Utv7eOEgbPemJB-Ng=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/grow-jackfruit-indoors-1902445-02-5c7391cd861e4a9a87ddb11eebecc922.jpg",
            name = "Jackfruit",
            description = "The tropical jackfruit tree (Artocarpus heterophyllus) is a large evergreen tree that bears edible fruit. It has a relatively fast growth rate, and new trees can start producing fruit within a few years. It’s best planted in the spring. The tree trunk grows fairly straight and has reddish-brown bark. From it extend large branches with glossy green leaves that are around 8 inches long. The tree produces showy green flowers mostly in the fall, though it can bloom sporadically at other points in the year. Its yellow-green, kidney bean-shaped fruits are notoriously huge, maturing on the tree in the midsummer. They average between 10 and 40 pounds, though some fruits have been known to weigh 80 pounds or more. The inner yellow flesh is mildly sweet and has been compared to bananas and pineapples."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/wFm7wd800OqfpvlK0GHdjHTfHVI=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/nemesia-plant-profile-5070320-11-f8453f1be78541a7930b4d61c19d2a3f.jpg",
            name = "Nemesia",
            description = "The Nemesia plant may resemble an orchid, but it’s actually a small bedding plant that has an array of implications for any garden. Often incorporated into landscaping as edging plants or ground covers—as well as in mixed borders, woodland plantings, rock gardens, and hanging basket plants—the Nemesia is a vibrant and maintenance flower.\n" +
                    "\n" +
                    "The two most popular species are N. strumosa and N. caerulea. Most varieties of Nemesia can grow to about a foot in height, but some can be as tall as two feet. The versatile plants also come in a wide range of colors, and some are even bi-colored. The N. strumosa variety is a true annual that produces one-inch blue or white flowers and grows up to a foot tall, while N. caerulea is a more tender perennial that grows up to about a half-inch and blooms in purple, pink, blue, and white on plants that grow up to two feet.\n" +
                    "\n" +
                    "At a distance, these plants may also look like tiny snapdragons or edging lobelia, with flowers that can completely cover low-growing foliage (the top four petals form a fan with one large and sometimes lobed petal underneath). As long as temperatures remain mild, the plant can produce so many flowers that they almost entirely obscure any other foliage."
        ),
        Plant(
            imageUrl = "https://www.thespruce.com/thmb/-W1mZNWR5tVwvp4reWuEoe0ZEyc=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/GettyImages-902133540-7e603f4ab7aa4aadb097c61d2627da24.jpg",
            name = "Quinoa",
            description = "Quinoa (Chenopodium quinoa) is a flowering plant in the Amaranth family that is grown as a crop primarily for its edible seeds. It has been grown for human consumption for thousands of years, originating from mountainous regions of South America. Quinoa cultivation has now grown to over 70 countries around the world. This ancient superfood is packed with vitamins and minerals and has a nice mild taste. The seeds are often cooked like rice, or ground into a flour that can be used as a gluten-free alternative in cooking and baking."
        ),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plant_list, parent, false)

        return PlantListViewHolder(view, listener)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        val plant = items[holder.absoluteAdapterPosition]

        holder.bind(plant)
    }
}