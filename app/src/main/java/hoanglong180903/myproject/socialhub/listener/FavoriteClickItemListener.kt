package hoanglong180903.myproject.socialhub.listener

import hoanglong180903.myproject.socialhub.model.Favorite
import hoanglong180903.myproject.socialhub.model.Track

interface FavoriteClickItemListener {
    fun onItemClickDelete(favorite: Favorite , position : Int)

    fun onItemClickPlayAndPause(favorite: Favorite)
}