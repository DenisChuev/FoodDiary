package dc.food_diary.ui.main_tabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import dc.food_diary.ui.main_tabs.journal.FoodJournalFragment;

public class MainTabsViewPagerAdapter extends FragmentPagerAdapter {
    public MainTabsViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FoodJournalFragment();
        }
        else {
            return new StatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Журнал";
        } else {
            return "Статистика";
        }
    }
}
