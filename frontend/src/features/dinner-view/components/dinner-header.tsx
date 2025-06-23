import PageHeader from "@/components/shared/page-header";
import { PreDinnerInfoDto } from "@/types";

type Props = {
  preDinnerInfo: PreDinnerInfoDto;
};

const DinnerHeader = ({ preDinnerInfo }: Props) => (
  <header className="w-full border-b-4 border-black pb-4">
    <PageHeader title={preDinnerInfo.storyTitle} className="text-center" />
    <img
      src={`data:image/jpeg;base64,${preDinnerInfo.storyBannerData}`}
      alt="Story Banner"
      className="w-full h-48 md:h-64 object-cover border-2 border-black mb-4"
    />
    <div className="flex flex-col md:flex-row justify-between items-start md:items-end text-sm mt-4">
      <p className="mb-2 md:mb-0">
        <span className="font-bold">DATE/TIME:</span>{" "}
        {preDinnerInfo.dateTime &&
          new Date(preDinnerInfo.dateTime).toLocaleDateString(undefined, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
      </p>
      <p>
        <span className="font-bold">HOST:</span> {preDinnerInfo.host.name}
      </p>
    </div>
  </header>
);

export default DinnerHeader;
