package com.example.dogwalker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dogwalker.adapter.CommentAdapter
import com.example.dogwalker.data.Comment
import com.example.dogwalker.databinding.ActivitySinglePostBinding
import kotlinx.android.synthetic.main.comment_item.view.*

class SinglePostActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySinglePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySinglePostBinding.inflate(LayoutInflater.from(this))

        val dummyData = listOf(
            Comment(
                image = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg",
                name = "Kevin",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sagittis nisl diam, a molestie neque commodo id. Suspendisse imperdiet neque convallis varius dapibus. Quisque sodales imperdiet blandit. Cras non aliquam risus. Pellentesque et lectus tempus, dignissim ipsum a, volutpat magna. Phasellus aliquam lectus ac dui fermentum, gravida blandit lectus varius. Integer aliquam urna nec blandit bibendum. Aenean dapibus mauris auctor erat malesuada finibus." +
                        "Nunc finibus mauris non interdum luctus. Morbi euismod elit ipsum, vitae bibendum diam mattis et. Nulla dignissim, libero nec aliquet varius, felis turpis porttitor lacus, aliquam blandit justo mi finibus magna. Sed elementum scelerisque erat ac blandit. Aliquam vulputate sagittis ante at tempor. Sed eleifend molestie lacus vitae commodo. Fusce non tincidunt mauris. Pellentesque tempus dolor sit amet facilisis venenatis. Praesent aliquet gravida lacus, quis scelerisque velit mattis nec. Phasellus eros ex, faucibus a diam et, eleifend rhoncus arcu. Maecenas congue ante vel felis venenatis, in gravida mi vestibulum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent ac sapien et metus mollis vulputate id sed massa. Aenean blandit nulla vel aliquet consectetur." +
                        "" +
                        "Maecenas bibendum non enim a porta. Praesent malesuada lectus ullamcorper ex posuere tempor. Nulla ipsum lacus, vulputate non sollicitudin et, lobortis quis lectus. Maecenas pellentesque tempus condimentum. Nunc ac risus justo. Donec scelerisque tortor nisl, ut hendrerit nibh vulputate id. Donec sapien lorem, bibendum ac massa non, eleifend feugiat erat." +
                        "" +
                        "Aenean imperdiet diam ac justo pharetra venenatis. Suspendisse dapibus nulla lorem. Nullam id tellus eget nibh vehicula cursus. Sed posuere turpis et enim euismod accumsan. Cras sem ex, blandit viverra luctus et, accumsan ac dolor. Duis vel felis vitae nisl maximus porttitor. Donec at enim aliquet, tincidunt lorem eget, commodo diam. Nunc orci nisl, placerat a imperdiet eu, malesuada blandit enim. Sed placerat gravida justo in lobortis. Praesent elit augue, pharetra ac maximus non, feugiat ac lectus. Donec ullamcorper, turpis at imperdiet mattis, elit turpis mollis nibh, non venenatis velit diam id tellus. Curabitur id vestibulum ipsum, ac congue sapien. Vestibulum dolor ligula, tempor consequat elementum in, feugiat quis dui. Etiam sed ex congue, elementum neque at, viverra mi. Aenean eu condimentum purus, at sollicitudin odio. Maecenas sit amet ultrices mi." +
                        "" +
                        "Sed ex tortor, tempor eu tortor eu, faucibus dictum ligula. Duis ultrices ut urna sed bibendum. Donec consequat lectus a sapien tristique, eu rhoncus urna hendrerit. Etiam rutrum lorem nunc, vitae fringilla ex dapibus eu. Nunc ex risus, scelerisque pulvinar dolor in, feugiat faucibus velit. Integer ullamcorper laoreet arcu, et aliquam ligula. Aenean quis vehicula purus, a rhoncus ex. Vestibulum semper tincidunt magna, ac posuere risus rutrum eget. Duis eu bibendum ligula. Cras eleifend eget orci nec mollis. Integer condimentum bibendum felis at mattis. In tristique, nibh sit amet dictum auctor, augue nibh lacinia urna, id placerat nisl orci a sem. Maecenas ut suscipit quam. Nunc porttitor vestibulum elit ac condimentum. Nulla facilisi.",
                date = "09 september 1998"
            ),
            Comment(
                image = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg",
                name = "Kevin",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sagittis nisl diam, a molestie neque commodo id. Suspendisse imperdiet neque convallis varius dapibus. Quisque sodales imperdiet blandit. Cras non aliquam risus. Pellentesque et lectus tempus, dignissim ipsum a, volutpat magna. Phasellus aliquam lectus ac dui fermentum, gravida blandit lectus varius. Integer aliquam urna nec blandit bibendum. Aenean dapibus mauris auctor erat malesuada finibus." +
                        "Nunc finibus mauris non interdum luctus. Morbi euismod elit ipsum, vitae bibendum diam mattis et. Nulla dignissim, libero nec aliquet varius, felis turpis porttitor lacus, aliquam blandit justo mi finibus magna. Sed elementum scelerisque erat ac blandit. Aliquam vulputate sagittis ante at tempor. Sed eleifend molestie lacus vitae commodo. Fusce non tincidunt mauris. Pellentesque tempus dolor sit amet facilisis venenatis. Praesent aliquet gravida lacus, quis scelerisque velit mattis nec. Phasellus eros ex, faucibus a diam et, eleifend rhoncus arcu. Maecenas congue ante vel felis venenatis, in gravida mi vestibulum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent ac sapien et metus mollis vulputate id sed massa. Aenean blandit nulla vel aliquet consectetur." +
                        "" +
                        "Maecenas bibendum non enim a porta. Praesent malesuada lectus ullamcorper ex posuere tempor. Nulla ipsum lacus, vulputate non sollicitudin et, lobortis quis lectus. Maecenas pellentesque tempus condimentum. Nunc ac risus justo. Donec scelerisque tortor nisl, ut hendrerit nibh vulputate id. Donec sapien lorem, bibendum ac massa non, eleifend feugiat erat." +
                        "" +
                        "Aenean imperdiet diam ac justo pharetra venenatis. Suspendisse dapibus nulla lorem. Nullam id tellus eget nibh vehicula cursus. Sed posuere turpis et enim euismod accumsan. Cras sem ex, blandit viverra luctus et, accumsan ac dolor. Duis vel felis vitae nisl maximus porttitor. Donec at enim aliquet, tincidunt lorem eget, commodo diam. Nunc orci nisl, placerat a imperdiet eu, malesuada blandit enim. Sed placerat gravida justo in lobortis. Praesent elit augue, pharetra ac maximus non, feugiat ac lectus. Donec ullamcorper, turpis at imperdiet mattis, elit turpis mollis nibh, non venenatis velit diam id tellus. Curabitur id vestibulum ipsum, ac congue sapien. Vestibulum dolor ligula, tempor consequat elementum in, feugiat quis dui. Etiam sed ex congue, elementum neque at, viverra mi. Aenean eu condimentum purus, at sollicitudin odio. Maecenas sit amet ultrices mi." +
                        "" +
                        "Sed ex tortor, tempor eu tortor eu, faucibus dictum ligula. Duis ultrices ut urna sed bibendum. Donec consequat lectus a sapien tristique, eu rhoncus urna hendrerit. Etiam rutrum lorem nunc, vitae fringilla ex dapibus eu. Nunc ex risus, scelerisque pulvinar dolor in, feugiat faucibus velit. Integer ullamcorper laoreet arcu, et aliquam ligula. Aenean quis vehicula purus, a rhoncus ex. Vestibulum semper tincidunt magna, ac posuere risus rutrum eget. Duis eu bibendum ligula. Cras eleifend eget orci nec mollis. Integer condimentum bibendum felis at mattis. In tristique, nibh sit amet dictum auctor, augue nibh lacinia urna, id placerat nisl orci a sem. Maecenas ut suscipit quam. Nunc porttitor vestibulum elit ac condimentum. Nulla facilisi.",
                date = "09 september 1998"
            )
        )

        binding.commentView.adapter = CommentAdapter(dummyData)
        binding.commentView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.commentView.isNestedScrollingEnabled = false

        binding.contentText.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sagittis nisl diam, a molestie neque commodo id. Suspendisse imperdiet neque convallis varius dapibus. Quisque sodales imperdiet blandit. Cras non aliquam risus. Pellentesque et lectus tempus, dignissim ipsum a, volutpat magna. Phasellus aliquam lectus ac dui fermentum, gravida blandit lectus varius. Integer aliquam urna nec blandit bibendum. Aenean dapibus mauris auctor erat malesuada finibus." +
                "Nunc finibus mauris non interdum luctus. Morbi euismod elit ipsum, vitae bibendum diam mattis et. Nulla dignissim, libero nec aliquet varius, felis turpis porttitor lacus, aliquam blandit justo mi finibus magna. Sed elementum scelerisque erat ac blandit. Aliquam vulputate sagittis ante at tempor. Sed eleifend molestie lacus vitae commodo. Fusce non tincidunt mauris. Pellentesque tempus dolor sit amet facilisis venenatis. Praesent aliquet gravida lacus, quis scelerisque velit mattis nec. Phasellus eros ex, faucibus a diam et, eleifend rhoncus arcu. Maecenas congue ante vel felis venenatis, in gravida mi vestibulum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent ac sapien et metus mollis vulputate id sed massa. Aenean blandit nulla vel aliquet consectetur." +
                "" +
                "Maecenas bibendum non enim a porta. Praesent malesuada lectus ullamcorper ex posuere tempor. Nulla ipsum lacus, vulputate non sollicitudin et, lobortis quis lectus. Maecenas pellentesque tempus condimentum. Nunc ac risus justo. Donec scelerisque tortor nisl, ut hendrerit nibh vulputate id. Donec sapien lorem, bibendum ac massa non, eleifend feugiat erat." +
                "" +
                "Aenean imperdiet diam ac justo pharetra venenatis. Suspendisse dapibus nulla lorem. Nullam id tellus eget nibh vehicula cursus. Sed posuere turpis et enim euismod accumsan. Cras sem ex, blandit viverra luctus et, accumsan ac dolor. Duis vel felis vitae nisl maximus porttitor. Donec at enim aliquet, tincidunt lorem eget, commodo diam. Nunc orci nisl, placerat a imperdiet eu, malesuada blandit enim. Sed placerat gravida justo in lobortis. Praesent elit augue, pharetra ac maximus non, feugiat ac lectus. Donec ullamcorper, turpis at imperdiet mattis, elit turpis mollis nibh, non venenatis velit diam id tellus. Curabitur id vestibulum ipsum, ac congue sapien. Vestibulum dolor ligula, tempor consequat elementum in, feugiat quis dui. Etiam sed ex congue, elementum neque at, viverra mi. Aenean eu condimentum purus, at sollicitudin odio. Maecenas sit amet ultrices mi." +
                "" +
                "Sed ex tortor, tempor eu tortor eu, faucibus dictum ligula. Duis ultrices ut urna sed bibendum. Donec consequat lectus a sapien tristique, eu rhoncus urna hendrerit. Etiam rutrum lorem nunc, vitae fringilla ex dapibus eu. Nunc ex risus, scelerisque pulvinar dolor in, feugiat faucibus velit. Integer ullamcorper laoreet arcu, et aliquam ligula. Aenean quis vehicula purus, a rhoncus ex. Vestibulum semper tincidunt magna, ac posuere risus rutrum eget. Duis eu bibendum ligula. Cras eleifend eget orci nec mollis. Integer condimentum bibendum felis at mattis. In tristique, nibh sit amet dictum auctor, augue nibh lacinia urna, id placerat nisl orci a sem. Maecenas ut suscipit quam. Nunc porttitor vestibulum elit ac condimentum. Nulla facilisi."

        val imgUri = dummyData[0].image.toUri().buildUpon().scheme("https").build()
        val imageView = binding.profilePost

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)

        setContentView(binding.root)
    }
}