package hoanglong180903.myproject.socialhub.listener

import hoanglong180903.myproject.socialhub.model.Track

interface OnClickItemListener {
    fun onItemClick(track: Track)

    fun onItemClickFavorite(track: Track)

    fun onItemClickIntent(track : Track)
}