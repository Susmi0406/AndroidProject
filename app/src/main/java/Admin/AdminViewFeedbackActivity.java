package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.efishshop.Model.Cart;
import com.example.efishshop.Model.UserFeedback;
import com.example.efishshop.Prevalent.Prevalent;
import com.example.efishshop.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;

import ViewHolder.CartViewHolder;
import ViewHolder.FeedbackViewHolder;

public class AdminViewFeedbackActivity extends AppCompatActivity {

    private RecyclerView feedbackview;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference RootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_feedback);



        feedbackview = findViewById(R.id.feedback_list);

        layoutManager = new LinearLayoutManager(this);
        feedbackview.setLayoutManager(layoutManager);


        RootRef = FirebaseDatabase.getInstance().getReference()
                .child("Feedbacks");


    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserFeedback> options =
                new FirebaseRecyclerOptions.Builder<UserFeedback>()
                        .setQuery(RootRef, UserFeedback.class)
                        .build();

        FirebaseRecyclerAdapter<UserFeedback, FeedbackViewHolder> adapter =
                new FirebaseRecyclerAdapter<UserFeedback, FeedbackViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull UserFeedback model) {
                        holder.txtUserFeedbackName.setText(model.getUsername());
                        holder.txtUserFeedbackList.setText(model.getFeedbacks());


                    }

                    @NonNull
                    @Override
                    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_items_layout, parent, false);
                        FeedbackViewHolder holder = new FeedbackViewHolder(view);
                        return holder;
                    }
                };

        feedbackview.setAdapter(adapter);
        adapter.startListening();
    }



    }
