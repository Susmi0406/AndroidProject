package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efishshop.R;

import Interface.ItemClickListner;

public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtUserFeedbackName, txtUserFeedbackList;
    private ItemClickListner itemClickListner;

    public FeedbackViewHolder(@NonNull View itemView) {
        super(itemView);

        txtUserFeedbackName = itemView.findViewById(R.id.User_feedback_name);
        txtUserFeedbackList = itemView.findViewById(R.id.user_feedback_list);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);

    }

    public void SetItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
