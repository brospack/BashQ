package by.vshkl.bashq.ui.acticity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.R;

public class QuotesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.srl_update)
    SwipeRefreshLayout srlUpdate;
    @BindView(R.id.rv_quotes)
    RecyclerView rvQuotes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        ButterKnife.bind(QuotesActivity.this);

        setSupportActionBar(toolbar);

        initializeNavigationDrawer(QuotesActivity.this, toolbar, savedInstanceState);
    }

    //==================================================================================================================

    private void initializeNavigationDrawer(AppCompatActivity activity, Toolbar toolbar, Bundle savedInstanceState) {
        new DrawerBuilder().withActivity(activity)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nd_new).withIcon(R.drawable.ic_new).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.nd_random).withIcon(R.drawable.ic_random).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.nd_best).withIcon(R.drawable.ic_best).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.nd_rating).withIcon(R.drawable.ic_rating).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss).withIcon(R.drawable.ic_abyss).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss_top).withIcon(R.drawable.ic_abyss_top).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss_best).withIcon(R.drawable.ic_abyss_best).withIdentifier(7),
                        new PrimaryDrawerItem().withName(R.string.nd_comics).withIcon(R.drawable.ic_comics).withIdentifier(8),
                        new SectionDrawerItem().withName(R.string.nd_fav),
                        new PrimaryDrawerItem().withName(R.string.nd_fav_quotes).withIcon(R.drawable.ic_favourite).withIdentifier(9),
                        new PrimaryDrawerItem().withName(R.string.nd_fav_comics).withIcon(R.drawable.ic_favourite).withIdentifier(10),
                        new SectionDrawerItem().withName(R.string.nd_other),
                        new PrimaryDrawerItem().withName(R.string.nd_settings).withIcon(R.drawable.ic_settings).withIdentifier(11)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .build();
    }
}
