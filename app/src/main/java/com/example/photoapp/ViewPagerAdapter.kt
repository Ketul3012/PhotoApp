package com.example.photoapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager , BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StorageFolderFragment()
            1 -> MainImageFragment()
            2 -> SettingsFragment()
            else -> StorageFolderFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

   /* override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Photos"
            1 -> "Explore"
            2 -> "Setting"
            else -> "Photos"
        }
    }
*/
}