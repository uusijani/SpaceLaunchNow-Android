package me.calebjones.spacelaunchnow.ui.main.vehicles.orbiter;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.ArrayList;
import java.util.List;

import me.calebjones.spacelaunchnow.R;
import me.calebjones.spacelaunchnow.content.database.ListPreferences;
import me.calebjones.spacelaunchnow.data.models.main.Agency;
import me.calebjones.spacelaunchnow.data.models.main.Orbiter;
import me.calebjones.spacelaunchnow.utils.GlideApp;
import me.calebjones.spacelaunchnow.utils.OnItemClickListener;
import timber.log.Timber;

/**
 * This adapter takes data from ListPreferences/LoaderService and applies it to the UpcomingLaunchesFragment
 */
public class OrbiterAdapter extends RecyclerView.Adapter<OrbiterAdapter.ViewHolder> {

    public int position;
    private Context mContext;
    private List<Agency> agencies = new ArrayList<Agency>();
    private OnItemClickListener onItemClickListener;
    private boolean night = false;
    private RequestOptions requestOptions;
    private int palette;

    public OrbiterAdapter(Context context) {
        agencies = new ArrayList();
        this.mContext = context;

        night = ListPreferences.getInstance(mContext).isNightModeActive(mContext);

        if (ListPreferences.getInstance(context).isNightModeActive(context)) {
            palette = GlidePalette.Profile.MUTED_DARK;
        } else {
            palette = GlidePalette.Profile.VIBRANT;
        }

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .centerCrop();
    }

    public void addItems(List<Agency> items) {
        if (this.agencies == null) {
            this.agencies = items;
        } else if (this.agencies.size() == 0) {
            this.agencies.addAll(items);
        } else {
            this.agencies.clear();
            this.agencies.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_item, viewGroup, false);
        return new ViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        final Agency agency = agencies.get(i);
        Timber.v("onBindViewHolder %s", agency.getName());

        holder.name.setText(agency.getName());
        holder.subTitle.setText(agency.getType());

        if (agency.getOrbiters() != null && agency.getOrbiters().size() > 0) {
            Orbiter firstOrbiter = agency.getOrbiters().get(0);

            if (firstOrbiter.getImageURL() != null) {
                GlideApp.with(mContext)
                        .load(firstOrbiter.getImageURL())
                        .apply(requestOptions)
                        .listener(GlidePalette.with(firstOrbiter.getImageURL())
                                .use(palette)
                                .intoCallBack(palette -> {
                                    Palette.Swatch color = null;
                                    if (palette != null) {
                                        if (night) {
                                            if (palette.getDarkMutedSwatch() != null) {
                                                color = palette.getDarkMutedSwatch();
                                            } else if (palette.getDarkVibrantSwatch() != null) {
                                                color = palette.getDarkVibrantSwatch();
                                            }
                                        } else {
                                            if (palette.getVibrantSwatch() != null) {
                                                color = palette.getVibrantSwatch();
                                            } else if (palette.getMutedSwatch() != null) {
                                                color = palette.getMutedSwatch();
                                            }
                                        }
                                        if (color != null) {
                                            holder.textContainer.setBackgroundColor(color.getRgb());
                                        }
                                    }
                                })
                                .crossfade(true))
                        .into(holder.picture);
            }
            holder.subTitle.setText(firstOrbiter.getName());
        }

    }

    @Override
    public int getItemCount() {
        return agencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View grid_root;
        public ImageView picture;
        public TextView name;
        public TextView subTitle;
        public View textContainer;
        private OnItemClickListener onItemClickListener;
        protected boolean animated = false;

        //Add content to the card
        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);

            this.onItemClickListener = onItemClickListener;
            grid_root = view.findViewById(R.id.grid_root);
            picture = view.findViewById(R.id.picture);
            name = view.findViewById(R.id.text);
            subTitle = view.findViewById(R.id.text_subtitle);
            textContainer = view.findViewById(R.id.text_container);
            grid_root.setOnClickListener(this);
        }

        //React to click events.
        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            Timber.d("%s clicked.", position);
            onItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
