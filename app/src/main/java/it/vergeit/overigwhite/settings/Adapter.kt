package it.vergeit.overigwhite.settings

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import it.vergeit.overigwhite.R

class Adapter(private val context: Context, private val settings: List<IBaseSettings>) : RecyclerView.Adapter<Adapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //Three layouts possible - Header, icon and switch
        if (viewType == 0) { //Header
            return ViewHolder(layoutInflater.inflate(R.layout.item_header, parent, false))
        } else if (viewType == 1) { //Icon Item
            return ViewHolder(layoutInflater.inflate(R.layout.item_preference_icon, parent, false))
        } else if (viewType == 2) { //Switch Item
            return ViewHolder(layoutInflater.inflate(R.layout.item_preference_switch, parent, false))
        } else if (viewType == 3) { //Button Item
            return ViewHolder(layoutInflater.inflate(R.layout.item_preference_button, parent, false))
        } else if (viewType == 4) { //Seekbar Item
            return ViewHolder(layoutInflater.inflate(R.layout.item_preference_seekbar, parent, false))
        } else if (viewType == 5) { //Incremental Item
            return ViewHolder(layoutInflater.inflate(R.layout.item_preference_increment, parent, false))
        }
        throw NotImplementedError()
    }

    override fun getItemViewType(position: Int): Int { //Return the type of a given item
        val setting = settings[position]
        if (setting is HeaderSetting) return 0
        if (setting is IconSetting) return 1
        if (setting is SwitchSetting) return 2
        if (setting is ButtonSetting) return 3
        return if (setting is SeekbarSetting) 4 else 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = settings[position]
        if (setting is HeaderSetting) { //Header, just set text
            holder.title.text = setting.title
        } else if (setting is SwitchSetting) { //Switch, setup the change listener and click listener for the root view
            val switchSetting = setting
            holder.sw.setOnCheckedChangeListener(switchSetting.changeListener)
            holder.root.setOnClickListener { holder.sw.toggle() }
            //Set default check
            holder.sw.isChecked = switchSetting.isChecked
            //Setup title
            holder.title.text = switchSetting.title
            //Setup subtitle if required
            holder.subtitle.text = switchSetting.subtitle
            holder.subtitle.visibility = View.VISIBLE
        } else if (setting is ButtonSetting) { //Icon, setup icon, click listener and title
            holder.root.setOnClickListener(setting.onClickListener)
            holder.title.text = setting.title
            holder.title.background = setting.bg
        } else if (setting is SeekbarSetting) { //Icon, setup icon, click listener and title
            //holder.icon.setImageDrawable(seekbarSetting.icon);
            holder.sb.max = setting.max
            holder.sb.progress = setting.current
            holder.sb.setOnSeekBarChangeListener(setting.onChangeListener)
            holder.title.text = setting.title
            holder.subtitle.text = setting.subtitle
            holder.subtitle.visibility = View.VISIBLE
        } else if (setting is IncrementalSetting) { //Icon, setup icon, click listeners, title, value
            holder.minus.setOnClickListener(setting.onClickLessListener)
            holder.plus.setOnClickListener(setting.onClickMoreListener)
            holder.title.text = setting.title
            holder.subtitle.text = setting.subtitle
            holder.value.text = setting.value
        } else { //Icon, setup icon, click listener and title
            val iconSetting = setting as IconSetting
            holder.icon.setImageDrawable(iconSetting.icon)
            holder.root.setOnClickListener(iconSetting.onClickListener)
            holder.title.text = iconSetting.title
            holder.icon.imageTintList = ColorStateList.valueOf(iconSetting.color)
            //Setup subtitle if required
            holder.subtitle.text = iconSetting.subtitle
            holder.subtitle.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return settings.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root: View
        var title: TextView
        var subtitle: TextView
        var minus: TextView
        var plus: TextView
        var value: TextView
        var icon: ImageView
        var sw: Switch
        var sb: SeekBar

        init {
            //Set views
            title = itemView.findViewById<View>(R.id.title) as TextView
            subtitle = itemView.findViewById<View>(R.id.subtitle) as TextView
            icon = itemView.findViewById<View>(R.id.icon) as ImageView
            sw = itemView.findViewById<View>(R.id.sw) as Switch
            sb = itemView.findViewById<View>(R.id.seekBar) as SeekBar
            minus = itemView.findViewById<View>(R.id.decrease) as TextView
            plus = itemView.findViewById<View>(R.id.increase) as TextView
            value = itemView.findViewById<View>(R.id.value) as TextView
            root = itemView
        }
    }



}