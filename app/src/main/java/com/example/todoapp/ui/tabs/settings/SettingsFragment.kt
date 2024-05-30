package com.example.todoapp.ui.tabs.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import java.util.Locale


class SettingsFragment : Fragment() {
    private lateinit var languageMenu: AutoCompleteTextView
    private lateinit var modeMenu: AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageMenu = requireView().findViewById(R.id.language_menu)
        modeMenu = requireView().findViewById(R.id.mode_menu)
        setMenus(languageMenu, resources.getStringArray(R.array.languages_array))
        setMenus(modeMenu, resources.getStringArray(R.array.mode_array))
        languageMenu.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                val localeListCompat = LocaleListCompat.create(Locale("en"))
                AppCompatDelegate.setApplicationLocales(localeListCompat)
            } else if (position == 1) {
                val localeListCompat = LocaleListCompat.create(Locale("ar"))

                AppCompatDelegate.setApplicationLocales(localeListCompat)
            }
        }
        modeMenu.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else if (position == 1) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun setMenus(menu: AutoCompleteTextView, stringArray: Array<String>) {
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, stringArray)
        menu.setAdapter(arrayAdapter)
    }
}