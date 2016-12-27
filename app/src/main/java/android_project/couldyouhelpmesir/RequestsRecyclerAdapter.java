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
        holder.descriptionView.setText(request.problem);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestsViewHolder extends RecyclerView.ViewHolder {

        //final TextView firstnameView;
        //final TextView secondnameView;
        final TextView descriptionView;

        private RequestsViewHolder(View itemView) {
            super(itemView);
            descriptionView = (TextView) itemView.findViewById(R.id.request_description);
        }

        static RequestsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_request, parent, false);
            return new RequestsViewHolder(view);
        }
    }
}
