package android_project.couldyouhelpmesir;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class RequestsRecyclerAdapter
        extends RecyclerView.Adapter<RequestsRecyclerAdapter.RequestsViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Request> requests = Collections.emptyList();

    public RequestsRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setRequests(@NonNull List<Request> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    @Override
    public RequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RequestsViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(RequestsViewHolder holder, int position) {
        final Request request = requests.get(position);
        //holder.firstnameView.setText(request.first_name);
        //holder.secondnameView.setText(request.second_name);
        holder.descriptionView.setText("ОПИСАНИЕ: " + request.problem);
        holder.firstNameView.setText("ИМЯ: " + request.first_name);
        holder.secondNameView.setText("ФАМИЛИЯ: " + request.second_name);
        holder.emailView.setText("EMAIL: " + request.email);
        holder.phoneNumberView.setText("PHONE NUMBER: " + request.phone_number);
        if (request.type == 0) holder.labelView.setText("ТИП: ПОЖАР");
        if (request.type == 1) holder.labelView.setText("ТИП: НАПАДЕНИЕ");
        if (request.type == 2) holder.labelView.setText("ТИП: МЕДПОМОЩЬ");
        if (request.type == 3) holder.labelView.setText("ТИП: УГОН");
        if (request.type == 4) holder.labelView.setText("ТИП: ТЕРАКТ");
        if (request.type == 5) holder.labelView.setText("ТИП: ФИЗ.СИЛА");
        if (request.type == -1) holder.labelView.setText("ТИП: НЕИЗВЕСТНО");
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestsViewHolder extends RecyclerView.ViewHolder {

        //final TextView firstnameView;
        //final TextView secondnameView;
        final TextView descriptionView;
        final TextView labelView;
        final TextView firstNameView;
        final TextView secondNameView;
        final TextView emailView;
        final TextView phoneNumberView;

        private RequestsViewHolder(View itemView) {
            super(itemView);
            descriptionView = (TextView) itemView.findViewById(R.id.request_description);
            labelView = (TextView) itemView.findViewById(R.id.request_label);
            firstNameView = (TextView) itemView.findViewById(R.id.request_first_name);
            secondNameView = (TextView) itemView.findViewById(R.id.request_second_name);
            emailView = (TextView) itemView.findViewById(R.id.request_email);
            phoneNumberView = (TextView) itemView.findViewById(R.id.request_phone_number);
        }

        static RequestsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_request, parent, false);
            return new RequestsViewHolder(view);
        }
    }
}
