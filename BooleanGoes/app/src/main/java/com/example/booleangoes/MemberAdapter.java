package com.example.booleangoes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private Context context;
    private ArrayList<Member> members;

    public MemberAdapter(Context context, ArrayList<Member> members) {
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);

        holder.textMemberHeader.setText("#" + member.memberNumber + "  " + member.fullName);

        holder.textMemberDob.setText("DOB: " + member.dateOfBirth);
        holder.textMemberGender.setText("Gender: " + member.gender);
        holder.textMemberPhone.setText("Phone: " + member.phoneNumber);
        holder.textMemberEmail.setText("Email: " + member.email);
        holder.textMemberCity.setText("City: " + member.city);

        holder.memberDetailsLayout.setVisibility(View.GONE);
        holder.btnExpandMember.setText("▼");

        holder.btnExpandMember.setOnClickListener(v -> {
            if (holder.memberDetailsLayout.getVisibility() == View.VISIBLE) {
                holder.memberDetailsLayout.setVisibility(View.GONE);
                holder.btnExpandMember.setText("▼");
            } else {
                holder.memberDetailsLayout.setVisibility(View.VISIBLE);
                holder.btnExpandMember.setText("▲");
            }
        });

        holder.btnEditMemberItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);

            intent.putExtra("memberNumber", member.memberNumber);
            intent.putExtra("fullName", member.fullName);
            intent.putExtra("dateOfBirth", member.dateOfBirth);
            intent.putExtra("gender", member.gender);
            intent.putExtra("phoneNumber", member.phoneNumber);
            intent.putExtra("email", member.email);
            intent.putExtra("city", member.city);

            intent.putExtra("fromViewPage", true);
            context.startActivity(intent);
        });

        holder.btnRemoveMemberItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, RemoveActivity.class);

            intent.putExtra("memberNumber", member.memberNumber);
            intent.putExtra("fullName", member.fullName);
            intent.putExtra("dateOfBirth", member.dateOfBirth);
            intent.putExtra("gender", member.gender);
            intent.putExtra("phoneNumber", member.phoneNumber);
            intent.putExtra("email", member.email);
            intent.putExtra("city", member.city);

            intent.putExtra("fromViewPage", true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView textMemberHeader;
        LinearLayout memberDetailsLayout;
        TextView textMemberDob;
        TextView textMemberGender;
        TextView textMemberPhone;
        TextView textMemberEmail;
        TextView textMemberCity;
        Button btnExpandMember;
        Button btnEditMemberItem;
        Button btnRemoveMemberItem;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);

            textMemberHeader = itemView.findViewById(R.id.textMemberHeader);
            memberDetailsLayout = itemView.findViewById(R.id.memberDetailsLayout);
            textMemberDob = itemView.findViewById(R.id.textMemberDob);
            textMemberGender = itemView.findViewById(R.id.textMemberGender);
            textMemberPhone = itemView.findViewById(R.id.textMemberPhone);
            textMemberEmail = itemView.findViewById(R.id.textMemberEmail);
            textMemberCity = itemView.findViewById(R.id.textMemberCity);
            btnExpandMember = itemView.findViewById(R.id.btnExpandMember);
            btnEditMemberItem = itemView.findViewById(R.id.btnEditMemberItem);
            btnRemoveMemberItem = itemView.findViewById(R.id.btnRemoveMemberItem);
        }
    }
}