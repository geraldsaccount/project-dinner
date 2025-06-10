import PageHeader from "@/components/shared/page-header";
import UserAvatar from "@/components/shared/user-avatar";
import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";

type Props = {
  dinner: GuestDinnerViewDto | HostDinnerViewDto;
};

const DinnerHeader = ({ dinner }: Props) => (
  <header className="flex justify-between">
    <div>
      <PageHeader title="Dinner Details" />
      <p className="text-lg text-muted-foreground mt-1">
        {dinner.dateTime &&
          new Date(dinner.dateTime).toLocaleDateString(undefined, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
      </p>
    </div>
    <div className="flex items-center mt-4 sm:mt-0">
      <UserAvatar className="w-16 h-16 mr-4" user={dinner.host} />
      <div>
        <p className="text-sm text-muted-foreground">Hosted by</p>
        <p className="font-semibold text-lg">{dinner.host.name}</p>
      </div>
    </div>
  </header>
);

export default DinnerHeader;
