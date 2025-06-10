import { InvitationViewDto } from "@/types";

type Props = {
  invitation: InvitationViewDto;
};

const StoryBanner = ({ invitation }: Props) => (
  <div className="relative w-full h-40 sm:h-60 md:h-80 lg:h-96 mb-4 rounded-xl shadow overflow-hidden">
    <img
      src={invitation.storyBannerUrl}
      alt={invitation.storyTitle}
      className="w-full h-full object-cover absolute inset-0"
    />
    <div className="absolute inset-0 bg-gradient-to-t from-primary/70 via-primary/30 to-transparent" />
    <div className="absolute bottom-0 left-0 w-full p-4 text-primary-foreground">
      <h3 className="text-2xl font-extrabold tracking-tight drop-shadow-lg">
        {invitation.storyTitle}
      </h3>
      <div className="text-base font-medium drop-shadow-lg">
        {invitation.dinnerStoryBrief}
      </div>
    </div>
  </div>
);

export default StoryBanner;
