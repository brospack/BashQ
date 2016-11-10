package by.vshkl.bashq.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import by.vshkl.bashq.R;

public class DrawerHelper {

    public static Drawer initializeDrawer(AppCompatActivity activity, Toolbar toolbar, Bundle savedInstanceState,
                                          Drawer.OnDrawerItemClickListener listener) {
        return new DrawerBuilder().withActivity(activity)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_new)
                                .withIcon(R.drawable.ic_new)
                                .withSelectedIcon(R.drawable.ic_new_selected)
                                .withIdentifier(1),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_random)
                                .withIcon(R.drawable.ic_random)
                                .withSelectedIcon(R.drawable.ic_random_selected)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_best)
                                .withIcon(R.drawable.ic_best)
                                .withSelectedIcon(R.drawable.ic_best_selected)
                                .withIdentifier(3),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_rating)
                                .withIcon(R.drawable.ic_rating)
                                .withSelectedIcon(R.drawable.ic_rating_selected)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_abyss)
                                .withIcon(R.drawable.ic_abyss)
                                .withSelectedIcon(R.drawable.ic_abyss_selected)
                                .withIdentifier(5),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_abyss_top)
                                .withIcon(R.drawable.ic_abyss_top)
                                .withSelectedIcon(R.drawable.ic_abyss_top_selected)
                                .withIdentifier(6),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_abyss_best)
                                .withIcon(R.drawable.ic_abyss_best)
                                .withSelectedIcon(R.drawable.ic_abyss_best_selected)
                                .withIdentifier(7),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_comics)
                                .withIcon(R.drawable.ic_comics)
                                .withSelectedIcon(R.drawable.ic_comics_selected)
                                .withIdentifier(8),
                        new SectionDrawerItem()
                                .withName(R.string.nd_fav),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_fav_quotes)
                                .withIcon(R.drawable.ic_favourite)
                                .withSelectedIcon(R.drawable.ic_favourite_selected)
                                .withIdentifier(9),
                        new SectionDrawerItem()
                                .withName(R.string.nd_other),
                        new PrimaryDrawerItem()
                                .withName(R.string.nd_settings)
                                .withIcon(R.drawable.ic_settings)
                                .withSelectedIcon(R.drawable.ic_settings_selected)
                                .withIdentifier(10)
                )
                .withOnDrawerItemClickListener(listener)
                .build();
    }
}
