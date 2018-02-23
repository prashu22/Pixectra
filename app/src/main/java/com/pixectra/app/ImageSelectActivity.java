package com.pixectra.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pixectra.app.Fragments.ImageFragment;
import com.pixectra.app.Utils.CartHolder;

public class ImageSelectActivity extends AppCompatActivity {
ViewPager viewPager;
TabLayout tabLayout;
    RecyclerView selectedImages;
    SelectedItemsAdapter selectedItemsAdapter;
    FloatingActionButton save;
    int w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        DisplayMetrics dm = new DisplayMetrics();
        ImageSelectActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels / 3;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar3));
        getSupportActionBar().setElevation(0);
        save = findViewById(R.id.save_images);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CartHolder.getInstance().getSize(getIntent().getStringExtra("key")) == getIntent().getIntExtra("pics", 0)) {
//                    Intent intent=new Intent(ImageSelectActivity.this,Cart.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(ImageSelectActivity.this, "Please Select "
                                    + (getIntent().getIntExtra("pics", 0)
                                    - CartHolder.getInstance().getSize(getIntent().getStringExtra("key")))
                                    + " More Images"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectedItemsAdapter = new SelectedItemsAdapter();
        viewPager=findViewById(R.id.image_select_pager);
        viewPager.setOffscreenPageLimit(3);
        selectedImages = findViewById(R.id.selected_images_recycler);
        selectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectedImages.setAdapter(selectedItemsAdapter);
        if (CartHolder.getInstance().getSize(getIntent().getStringExtra("key")) > 0) {
            selectedImages.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            selectedItemsAdapter.notifyDataSetChanged();
        }
        setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.image_select_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons(tabLayout);
        CartHolder.getInstance().setOnImageChangedListner(new CartHolder.ImageChangedListner() {
            @Override
            public void onImageAdded(Bitmap img, int size) {
                if (size > 0) {
                    save.setVisibility(View.VISIBLE);
                    selectedImages.setVisibility(View.VISIBLE);
                    selectedItemsAdapter.notifyDataSetChanged();
                }
                if (getSupportActionBar() != null)
                    setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
            }

            @Override
            public void onImageDeleted(Bitmap img, int size) {
                selectedItemsAdapter.notifyDataSetChanged();
                if (size > 0) {
                    save.setVisibility(View.VISIBLE);
                    selectedImages.setVisibility(View.VISIBLE);

                } else {
                    save.setVisibility(View.GONE);
                    selectedImages.setVisibility(View.GONE);

                }
                if (getSupportActionBar() != null)
                    setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
            }

            @Override
            public void alreadyPresent(Bitmap img) {
                Toast.makeText(ImageSelectActivity.this, "Image Already Present In Cart", Toast.LENGTH_SHORT).show();
                selectedItemsAdapter.notifyDataSetChanged();
            }
        });

    }

    void setToolbarText(int count) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Select Images (" + count + "/" + getIntent().getIntExtra("pics", 0) + ")");
    }
    void setTabIcons(TabLayout tabs){
        int[] icons = {R.drawable.device, R.drawable.facebook, R.drawable.instagram, R.drawable.google_photos};
        for (int i=0;i<4;i++)
            tabs.getTabAt(i).setIcon(icons[i]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0 && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack("albumactivity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_image_item, parent, false);
            return new SelectedItemsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(ImageSelectActivity.this).load(CartHolder.getInstance().getImage(getIntent().getStringExtra("key"), position)).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return CartHolder.getInstance().getSize(getIntent().getStringExtra("key"));
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);

                itemView.getLayoutParams().width = (int) ImageSelectActivity.this.getResources().getDimension(R.dimen.selected_images_dimen);
                itemView.getLayoutParams().height = (int) ImageSelectActivity.this.getResources().getDimension(R.dimen.selected_images_dimen);
                image = itemView.findViewById(R.id.ListIcon);
                itemView.findViewById(R.id.image_loading_progress).setVisibility(View.GONE);
                image.setOnClickListener(this);
                itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CartHolder.getInstance().removeImage(getIntent().getStringExtra("key")
                                , CartHolder.getInstance().getImage(getIntent().getStringExtra("key"), getAdapterPosition()));
                    }
                });
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageSelectActivity.this, CroppingActivity.class);
                intent.putExtra("key", getIntent().getStringExtra("key"));
                intent.putExtra("index", getAdapterPosition());
                ImageSelectActivity.this.startActivity(intent);

            }
        }
    }
}
